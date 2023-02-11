package com.example.cleaner_int_tests.web;

import com.example.cleaner_int_tests.data.RecipeEntity;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
class RecipePatchMerger {
    private final RecipeEntity entity;
    private final RecipeRequest request;

    void patchFields() {
        patchField(request::getName, entity::setName);
        patchField(request::getInstructions, entity::setInstructions);
        patchField(request::getIngredients, entity::setIngredientsList);
    }

    private <T> void patchField(Supplier<T> getter, Consumer<T> setter) {
        T value = getter.get();
        if (value != null) {
            setter.accept(value);
        }
    }

}
