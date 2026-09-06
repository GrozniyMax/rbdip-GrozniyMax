package com.rbdip.bookstore.review.controller.dto.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateReviewRequest(

        String authorName,

        @NotNull
        @Positive
        Integer rating,

        String comment

) {
}