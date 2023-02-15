package com.example.cleaner_int_tests.web;

import com.example.cleaner_int_tests.data.RecipeEntity;
import org.springframework.stereotype.Component;

@Component
class RecipesRestConverter {
    public RecipeResponse toResponse(RecipeEntity entity) {
        return RecipeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .instructions(entity.getInstructions())
                .ingredients(entity.getIngredientsList())
                .build();
    }

    public RecipeEntity convert(RecipeRequest request) {
        RecipeEntity entity = RecipeEntity.builder()
                .name(request.getName())
                .instructions(request.getInstructions())
                .build();
        entity.setIngredientsList(request.getIngredients());
        return entity;
    }

}
