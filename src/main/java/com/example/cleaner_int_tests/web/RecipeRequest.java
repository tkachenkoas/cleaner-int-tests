package com.example.cleaner_int_tests.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
class RecipeRequest {
    private String name;
    private String instructions;
    private List<String> ingredients;
}
