package org.g9project4.planner.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RequestPlanner {
    private Long seq; // 수정시 필요
    private String mode = "write";

    @NotBlank
    private String title; // 플래너명

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate sDate; // 여행시작일

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate eDate; // 여행종료일

    @NotBlank
    private String itinerary; // 여행일정
}
