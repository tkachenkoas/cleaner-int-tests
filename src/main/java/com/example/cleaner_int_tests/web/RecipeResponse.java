package com.example.cleaner_int_tests.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
class RecipeResponse extends RecipeRequest {
    private String id;

}
