package com.sss.cartnest.controllers;

import com.sss.cartnest.entities.Review;
import com.sss.cartnest.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;

    // GET /api/reviews?productId=1
    @GetMapping
    public ResponseEntity<?> getReviews(@RequestParam int productId) {
        List<Review> reviews = reviewRepo.findByProductId(productId);
        Double avgRating = reviewRepo.findAvgRatingByProductId(productId);
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews);
        response.put("avgRating", avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        response.put("totalReviews", reviews.size());
        return ResponseEntity.ok(response);
    }

    // POST /api/reviews/add
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody Map<String, Object> request) {
        try {
            Review review = new Review();
            review.setProductId((int) request.get("productId"));
            review.setUsername((String) request.get("username"));
            review.setRating((int) request.get("rating"));
            review.setComment((String) request.get("comment"));
            reviewRepo.save(review);
            return ResponseEntity.ok(Map.of("message", "Review added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
