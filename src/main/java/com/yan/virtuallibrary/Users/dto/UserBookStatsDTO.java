package com.yan.virtuallibrary.Users.dto;

public record UserBookStatsDTO(
        long totalRead,
        long totalThisYear,
        long totalWishlist,
        double averageRating
) {
}
