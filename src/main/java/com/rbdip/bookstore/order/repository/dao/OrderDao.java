package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.repository.CustomerRepository;
import com.rbdip.bookstore.order.repository.OrderRepository;
import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.OrderStatus;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DAO заказов. После нормализации схемы (ЛР2) заказ ссылается на клиента
 * ассоциацией {@code @ManyToOne} (внешний ключ customer_id), а доменная
 * модель {@link Order} остаётся неизменной: здесь (в слое DAO) сущность
 * превращается в домен через {@link CustomerMapper}.
 */
@Component
public class OrderDao extends AbstractDao<Order, com.rbdip.bookstore.order.repository.entities.Order, Long> {

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    protected OrderDao(
            OrderRepository repository,
            CustomerRepository customerRepository,
            CustomerMapper customerMapper) {
        // Генерик-маппер не используется: OrderDao сам строит домен и сущность
        // (см. create/findAll). Методы AbstractDao, опирающиеся на mapper
        // (save/findById/saveAll/...), для заказов не применяются.
        super(repository, new Mapper<Order, com.rbdip.bookstore.order.repository.entities.Order>() {
            @Override
            public com.rbdip.bookstore.order.repository.entities.Order map(Order domain) {
                throw new UnsupportedOperationException("Создание заказа выполняется через OrderDao#create");
            }

            @Override
            public Order unmap(com.rbdip.bookstore.order.repository.entities.Order entity) {
                throw new UnsupportedOperationException("Чтение заказа выполняется через OrderDao#findAll");
            }
        });
        this.orderRepository = repository;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllWithCustomer().stream()
                .map(this::toDomain)
                .toList();
    }

    public Order create(Long customerId, OrderStatus status) {
        // getReferenceById возвращает прокси без запроса; FK проставляется
        // при сохранении. Гидратация клиента допустима, т.к. create вызывается
        // внутри транзакции (OrderStorage#createOrder).
        var customerProxy = customerRepository.getReferenceById(customerId);
        var entity = new com.rbdip.bookstore.order.repository.entities.Order(customerProxy, status.getValue());
        return toDomain(orderRepository.save(entity));
    }

    private Order toDomain(com.rbdip.bookstore.order.repository.entities.Order entity) {
        Customer customer = customerMapper.unmap(entity.getCustomer());
        return new Order(
                entity.getId(),
                customer,
                OrderStatus.withValue(entity.getStatus()).orElse(OrderStatus.NEW),
                entity.getCreatedAt());
    }
}