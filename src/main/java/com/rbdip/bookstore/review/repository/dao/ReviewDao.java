package com.rbdip.bookstore.review.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.review.repository.ReviewRepository;
import com.rbdip.bookstore.review.service.domain.Review;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReviewDao extends AbstractDao<Review, com.rbdip.bookstore.review.repository.entities.Review, Long> {

    private final ReviewRepository reviewRepository;

    protected ReviewDao(
            ReviewRepository repository,
            Mapper<Review, com.rbdip.bookstore.review.repository.entities.Review> mapper) {
        super(repository, mapper);
        this.reviewRepository = repository;
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(mapper::unmap)
                .toList();
    }
}