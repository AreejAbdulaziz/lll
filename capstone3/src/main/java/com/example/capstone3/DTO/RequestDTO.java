package com.example.capstone3.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDTO {
    @NotNull(message = "member id cannot be null")
    @Positive(message = "enter correct member id")
    private Integer member_id;
    @NotNull(message = "team id cannot be null")
    @Positive(message = "enter correct team id")
    private Integer team_id;
}
