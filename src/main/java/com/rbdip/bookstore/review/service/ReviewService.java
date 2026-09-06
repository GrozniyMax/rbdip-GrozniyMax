package com.rbdip.bookstore.review.service;

import com.rbdip.bookstore.order.repository.OrderItemRepository;
import com.rbdip.bookstore.order.repository.OrderRepository;
import com.rbdip.bookstore.review.repository.dao.ReviewDao;
import com.rbdip.bookstore.review.service.domain.Review;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Намеренная архитектурная связанность: чтобы проверить, что автор отзыва
 * действительно покупал товар, сервис напрямую лезет во внутренние
 * репозитории пакета order, вместо обращения через выделенный контракт
 * (интерфейс/событие). Это цель для выделения модуля review по Strangler
 * Fig в ЛР4 - после рефакторинга ArchitectureRulesTest (пакет reference)
 * должен зафиксировать отсутствие такой зависимости.
 */
@Service
public class ReviewService {

    private final ReviewDao reviewDao;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewService(
            ReviewDao reviewDao,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.reviewDao = reviewDao;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Review addReview(Long productId, String authorName, Integer rating, String comment) {
        // NB: в текущей "грязной" версии verifiedPurchase не используется дальше,
        // но сам факт прямого обращения к order-репозиториям отсюда - и есть
        // намеренная связанность, которую нужно устранить.
        boolean verifiedPurchase = !orderRepository.findAll().isEmpty()
                && !orderItemRepository.findAll().isEmpty();
        Review review = new Review(
                null,
                productId,
                authorName == null ? "anonymous" : authorName,
                rating,
                comment,
                null);
        return reviewDao.save(review);
    }

    public List<Review> listReviews(Long productId) {
        return reviewDao.findByProductId(productId);
    }
}