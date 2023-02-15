package com.example.cleaner_int_tests;

import com.example.cleaner_int_tests.test_clients.RecipesRestApiTestClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudFlowTest_v2_WithTestClient extends BaseFullContextTest {

    @Autowired
    private RecipesRestApiTestClient apiClient;

    @Test
    @Order(0)
    void createNewRecipe() {
        Map response = apiClient.createRecipe(Map.of(
                "name", "Man sushi",
                "instructions", "Call sushi bar",
                "ingredients", List.of("Phone", "Sushi bar", "Money")
        ));

        assert response.get("name").equals("Man sushi");
        assert response.get("instructions").equals("Call sushi bar");
        assert response.get("ingredients").equals(
                List.of("Phone", "Sushi bar", "Money")
        );
    }

    String recipeId;

    @Test
    @Order(1)
    void createdRecipe_shouldBeAvailable_viaGetPage_andViaGetById() {
        Map<String, Object> responseAsMap = apiClient.getRecipesPage();

        List<Map<String, Object>> content = (List) responseAsMap.get("content");
        assert content.size() == 1;
        var recipe = content.get(0);
        assert recipe.get("name").equals("Man sushi");
        assert recipe.get("instructions").equals("Call sushi bar");
        assert recipe.get("ingredients").equals(List.of(
                "Phone", "Sushi bar", "Money"
        ));

        recipeId = (String) recipe.get("id");

        Map byIdRecipe = apiClient.getRecipe(recipeId);
        assertThat(byIdRecipe).isEqualTo(recipe);
    }

    @Test
    @Order(2)
    void shouldUpdateRecipeById_inPatchManner() {
        Map<String, Object> patchResponse = apiClient.updateRecipe(
                recipeId, Map.of("name", "Lazy sushi")
        );

        assert patchResponse.get("name").equals("Lazy sushi");
        assert patchResponse.get("instructions").equals("Call sushi bar");
    }

    @Test
    @Order(3)
    void reloadById_verifyNameUpdate() {
        Map byIdRecipe = apiClient.getRecipe(recipeId);

        assert byIdRecipe.get("name").equals("Lazy sushi");
        assert byIdRecipe.get("instructions").equals("Call sushi bar");
    }

    @Test
    @Order(4)
    void deleteById_shouldRemoveFrom_getPage() {
        apiClient.deleteRecipe(recipeId);

        Map pageResponse = apiClient.getRecipesPage();
        List<Map<String, Object>> content = (List) pageResponse.get("content");
        assert content.isEmpty();
    }

    @Test
    @Order(5)
    void getMissingRecipeById_shouldReturn404() {
        Map responseAsMap = apiClient.getRecipe(NOT_FOUND, recipeId);
        assert responseAsMap.get("message").equals("No recipe for id " + recipeId);
    }

}
