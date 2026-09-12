package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.repository.OrderItemRepository;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.product.domain.Product;
import com.rbdip.bookstore.product.repository.ProductRepository;
import com.rbdip.bookstore.product.repository.dao.ProductMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DAO позиций заказа. После нормализации схемы (ЛР2) позиция ссылается на
 * товар ассоциацией {@code @ManyToOne} (внешний ключ product_id), а доменная
 * модель {@link OrderItem} остаётся неизменной: здесь (в слое DAO) сущность
 * превращается в домен через {@link ProductMapper}.
 */
@Component
public class OrderItemDao extends AbstractDao<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem, Long> {

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    protected OrderItemDao(
            OrderItemRepository repository,
            ProductRepository productRepository,
            ProductMapper productMapper) {
        // Генерик-маппер не используется: OrderItemDao сам строит домен и
        // сущность (см. findAllByOrderIds/saveAll). Методы AbstractDao, опирающиеся
        // на mapper (findAll/findById/...), для позиций не применяются.
        super(repository, new Mapper<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem>() {
            @Override
            public com.rbdip.bookstore.order.repository.entities.OrderItem map(OrderItem domain) {
                throw new UnsupportedOperationException("Сохранение позиций выполняется через OrderItemDao#saveAll");
            }

            @Override
            public OrderItem unmap(com.rbdip.bookstore.order.repository.entities.OrderItem entity) {
                throw new UnsupportedOperationException("Чтение позиций выполняется через OrderItemDao#findAllByOrderIds");
            }
        });
        this.orderItemRepository = repository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<OrderItem> findAllByOrderIds(List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return List.of();
        }
        return orderItemRepository.findAllByOrderIdIn(orderIds).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<OrderItem> saveAll(Iterable<OrderItem> domains) {
        List<com.rbdip.bookstore.order.repository.entities.OrderItem> entities = new ArrayList<>();
        for (OrderItem item : domains) {
            entities.add(toEntity(item));
        }
        orderItemRepository.saveAll(entities);
        // Возврат вызывающим кодом не используется (см. OrderStorage#createOrder).
        return List.of();
    }

    private com.rbdip.bookstore.order.repository.entities.OrderItem toEntity(OrderItem domain) {
        // getReferenceById возвращает прокси без запроса; FK проставляется при сохранении.
        var productProxy = productRepository.getReferenceById(domain.product().id());
        return new com.rbdip.bookstore.order.repository.entities.OrderItem(
                domain.orderId(),
                productProxy,
                domain.quantity());
    }

    private OrderItem toDomain(com.rbdip.bookstore.order.repository.entities.OrderItem entity) {
        Product product = productMapper.unmap(entity.getProduct());
        return new OrderItem(entity.getId(), entity.getOrderId(), product, entity.getQuantity());
    }
}