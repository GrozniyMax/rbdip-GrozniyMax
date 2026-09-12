package com.rbdip.bookstore.review.service;

import com.rbdip.bookstore.review.repository.dao.ReviewDao;
import com.rbdip.bookstore.review.service.domain.Review;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Проверка того, что автор отзыва реально покупал товар, вынесена за явный
 * контракт {@link PurchaseVerifier} (реализация - в модуле order, см.
 * OrderPurchaseVerifier). Модуль review больше не зависит напрямую от
 * внутренних репозиториев пакета order (ЛР4, Strangler Fig).
 */
@Service
public class ReviewService {

    private final ReviewDao reviewDao;
    private final PurchaseVerifier purchaseVerifier;

    public ReviewService(ReviewDao reviewDao, PurchaseVerifier purchaseVerifier) {
        this.reviewDao = reviewDao;
        this.purchaseVerifier = purchaseVerifier;
    }

    public Review addReview(Long productId, String authorName, Integer rating, String comment) {
        // NB: verifiedPurchase не используется дальше, но обращение идёт через
        // порт PurchaseVerifier, а не во внутренности order.
        boolean verifiedPurchase = purchaseVerifier.isVerifiedPurchase(productId);
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