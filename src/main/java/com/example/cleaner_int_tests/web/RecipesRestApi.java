package com.example.cleaner_int_tests.web;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/recipes")
public interface RecipesRestApi {

    @GetMapping
    Page<RecipeResponse> getRecipesPage();

    @PostMapping
    RecipeResponse createRecipe(
            @RequestBody RecipeRequest request
    );

    @GetMapping("/{id}")
    RecipeResponse getRecipe(@PathVariable("id") String id);

    @PatchMapping("/{id}")
    RecipeResponse updateRecipe(
            @PathVariable("id") String id,
            @RequestBody RecipeRequest request
    );

    @DeleteMapping("/{id}")
    void deleteRecipe(@PathVariable("id") String id);

}
