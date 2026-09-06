package com.rbdip.bookstore.review.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.review.service.domain.Review;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link Review} и JPA-сущностью {@code entities.Review}.
 */
@Component
public class ReviewMapper implements Mapper<Review, com.rbdip.bookstore.review.repository.entities.Review> {

    @Override
    public com.rbdip.bookstore.review.repository.entities.Review map(Review domain) {
        return new com.rbdip.bookstore.review.repository.entities.Review(
                domain.productId(),
                domain.authorName(),
                domain.rating(),
                domain.comment());
    }

    @Override
    public Review unmap(com.rbdip.bookstore.review.repository.entities.Review entity) {
        return new Review(
                entity.getId(),
                entity.getProductId(),
                entity.getAuthorName(),
                entity.getRating(),
                entity.getComment(),
                entity.getCreatedAt());
    }
}