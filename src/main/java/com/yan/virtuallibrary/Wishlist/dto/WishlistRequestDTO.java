package com.yan.virtuallibrary.Wishlist.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record WishlistRequestDTO(
        Long bookId,
        @NotBlank String title,
        String author,
        BigDecimal price,
        String storeName,
        String storeUrl,
        String notes
) {
}
