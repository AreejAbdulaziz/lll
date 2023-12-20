package com.example.capstone3.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {
    @NotNull(message = "leader id cannot be null")
    @Positive(message = "enter correct leader id")
    private Integer member_id;
    @NotNull(message = "member id cannot be null")
    @Email(message = "enter correct email")
    private Email member2_email;
    @PositiveOrZero
    @NotNull(message = "rating cannot be null")
    private double rating;
    @NotNull(message = "comment cannot be null")
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String comment;
}
