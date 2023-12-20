package com.example.capstone3.Controller;

import com.example.capstone3.DTO.ReviewDTO;
import com.example.capstone3.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/get")
    public ResponseEntity getAllReviews(){
        return ResponseEntity.status(200).body(reviewService.getAllReviews());
    }
    @PostMapping("/add")
    public ResponseEntity addReview(@RequestBody@Valid ReviewDTO reviewDTO){
        reviewService.addReview(reviewDTO);
        return ResponseEntity.status(200).body("Review Added!");
    }
    @PutMapping("/update/{review_id}")
    public ResponseEntity updateReview(@RequestBody@Valid ReviewDTO reviewDTO,@PathVariable Integer review_id){
        reviewService.updateReview(reviewDTO, review_id);
        return ResponseEntity.status(200).body("Review Updated!");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReview(@PathVariable Integer id){
        reviewService.deleteReview(id);
        return ResponseEntity.status(200).body("Review Deleted!");
    }
}
