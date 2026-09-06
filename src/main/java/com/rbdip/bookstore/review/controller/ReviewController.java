package com.rbdip.bookstore.review.controller;

import com.rbdip.bookstore.review.controller.dto.in.CreateReviewRequest;
import com.rbdip.bookstore.review.controller.dto.out.ReviewCreationResult;
import com.rbdip.bookstore.review.controller.dto.out.ReviewDto;
import com.rbdip.bookstore.review.service.ReviewService;
import com.rbdip.bookstore.review.service.domain.Review;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewCreationResult addReview(@PathVariable Long productId, @RequestBody CreateReviewRequest request) {
        Review review = reviewService.addReview(
                productId,
                request.authorName(),
                request.rating(),
                request.comment());
        return new ReviewCreationResult(review.id());
    }

    @GetMapping("/products/{productId}/reviews")
    public List<ReviewDto> listReviews(@PathVariable Long productId) {
        return reviewService.listReviews(productId).stream()
                .map(r -> new ReviewDto(
                        r.authorName(),
                        r.rating(),
                        r.comment() == null ? "" : r.comment()))
                .toList();
    }
}