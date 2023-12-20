package com.example.capstone3.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamDTO {
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "description cannot be null")
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String description;
    @NotNull(message = "max capacity cannot be null")
    @Positive(message = "max capacity must be positive")
    private Integer maxCap;

    @NotNull(message = "hackathon id cannot be null")
    @Positive(message = "enter correct hackathon id")
    private Integer hackathon_id;
    @NotNull(message = "member id cannot be null")
    @Positive(message = "enter correct member id")
    private Integer member_id;
}
