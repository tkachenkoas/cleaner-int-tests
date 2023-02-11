package com.example.cleaner_int_tests.web;

import com.example.cleaner_int_tests.data.RecipeEntity;
import com.example.cleaner_int_tests.data.RecipeEntityRepository;
import com.example.cleaner_int_tests.data.ResourceNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RecipesController implements RecipesRestApi {

    private final RecipeEntityRepository repository;
    private final RecipesRestConverter converter;

    @Override
    public Page<RecipeResponse> getRecipesPage() {
        return repository.findAll(PageRequest.of(0, 10))
                .map(converter::toResponse);
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest request) {
        RecipeEntity entity = converter.convert(request);
        RecipeEntity saved = repository.save(entity);
        return converter.toResponse(saved);
    }

    @Override
    public RecipeResponse getRecipe(String id) {
        RecipeEntity recipe = loadRecipe(id);
        return converter.toResponse(recipe);
    }

    @Override
    public RecipeResponse updateRecipe(String id, RecipeRequest request) {
        RecipeEntity existing = loadRecipe(id);
        new RecipePatchMerger(existing, request).patchFields();
        return converter.toResponse(
                repository.save(existing)
        );
    }

    @Override
    public void deleteRecipe(String id) {
        repository.deleteById(id);
    }

    private RecipeEntity loadRecipe(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotExistException("No recipe for id " + id));
    }
}
