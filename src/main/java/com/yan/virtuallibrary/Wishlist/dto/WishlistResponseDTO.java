package com.yan.virtuallibrary.Wishlist.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WishlistResponseDTO(
        Long id,
        Long bookId,
        String title,
        String author,
        BigDecimal price,
        String storeName,
        String storeUrl,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
