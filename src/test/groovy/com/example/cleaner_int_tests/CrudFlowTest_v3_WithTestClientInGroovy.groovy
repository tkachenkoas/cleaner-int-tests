package com.example.cleaner_int_tests


import com.example.cleaner_int_tests.test_clients.RecipesRestApiTestClient
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired

import static org.assertj.core.api.Assertions.assertThat
import static org.springframework.http.HttpStatus.NOT_FOUND

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudFlowTest_v3_WithTestClientInGroovy extends BaseFullContextTest {

    @Autowired
    private RecipesRestApiTestClient apiClient

    @Test
    @Order(0)
    void createNewRecipe() {
        def response = apiClient.createRecipe([
                "name"        : "Man sushi",
                "instructions": "Call sushi bar",
                "ingredients" : ["Phone", "Sushi bar", "Money"]
        ])

        assert response["name"] == "Man sushi"
        assert response["instructions"] == "Call sushi bar"
        assert response["ingredients"] == ["Phone", "Sushi bar", "Money"]
        assert response.ingredients[0] == 'Phone'
    }

    String recipeId

    @Test
    @Order(1)
    void createdRecipe_shouldBeAvailable_viaGetPage_andViaGetById() {
        def content = apiClient.getRecipesPage()["content"] as List
        assert content.size() == 1
        var recipe = content.get(0)
        assert recipe["name"] == "Man sushi"
        assert recipe["instructions"] == "Call sushi bar"
        assert recipe["ingredients"] == ["Phone", "Sushi bar", "Money"]

        recipeId = recipe.id

        def byIdRecipe = apiClient.getRecipe(recipeId)
        assertThat(byIdRecipe).isEqualTo(recipe)
    }

    @Test
    @Order(2)
    void shouldUpdateRecipeById_inPatchManner() {
        def patchResponse = apiClient.updateRecipe(
                recipeId, ["name": "Lazy sushi"]
        )

        assert patchResponse.name == "Lazy sushi"
        assert patchResponse.instructions == "Call sushi bar"
    }

    @Test
    @Order(3)
    void reloadById_verifyNameUpdate() {
        def byIdRecipe = apiClient.getRecipe(recipeId)

        assert byIdRecipe.name == "Lazy sushi"
        assert byIdRecipe.instructions == "Call sushi bar"
    }

    @Test
    @Order(4)
    void deleteById_shouldRemoveFrom_getPage() {
        apiClient.deleteRecipe(recipeId)

        def content = apiClient.getRecipesPage().content
        assert content.isEmpty()
    }

    @Test
    @Order(5)
    void getMissingRecipeById_shouldReturn404() {
        def response = apiClient.getRecipe(NOT_FOUND, recipeId)
        assert response.message == "No recipe for id $recipeId"
    }

}
