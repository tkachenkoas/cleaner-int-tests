package com.example.cleaner_int_tests;

import com.example.cleaner_int_tests.test_utils.TestRestOperations;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static com.example.cleaner_int_tests.test_utils.MockMvcOperationsUtils.asserJsonPaths;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudFlowTest_v1_WithUtilsAndTestRestOperations extends BaseFullContextTest {

    @Autowired
    private TestRestOperations testRestOperations;

    @Test
    @Order(0)
    void createNewRecipe() {
        ResultActions postActions = testRestOperations.postActions(
                "/recipes",
                """
                        {
                        "name" : "Man sushi",
                        "instructions" : "Call sushi bar",
                        "ingredients" : [ "Phone", "Sushi bar", "Money" ]
                        }
                        """
        );

        asserJsonPaths(postActions, Map.of(
                "name", "Man sushi",
                "instructions", "Call sushi bar",
                "ingredients[0]", "Phone",
                "ingredients[1]", "Sushi bar",
                "ingredients[2]", "Money"
        ));
    }

    String recipeId;

    @Test
    @Order(1)
    void createdRecipe_shouldBeAvailable_viaGetPage_andViaGetById() {
        Map<String, Object> responseAsMap = testRestOperations.doGet("/recipes");

        List<Map<String, Object>> content = (List) responseAsMap.get("content");
        assertThat(content).hasSize(1);
        var recipe = content.get(0);
        assertThat(recipe.get("name")).isEqualTo("Man sushi");
        assertThat(recipe.get("instructions")).isEqualTo("Call sushi bar");
        assertThat((List) recipe.get("ingredients"))
                .hasSize(3)
                .containsExactly("Phone", "Sushi bar", "Money");

        recipeId = (String) recipe.get("id");

        Map byIdRecipe = testRestOperations.doGet("/recipes/" + recipeId);
        assertThat(byIdRecipe).isEqualTo(recipe);
    }

    @Test
    @Order(2)
    void shouldUpdateRecipeById_inPatchManner() {
        Map<String, Object> patchResponse = testRestOperations.doPatch(
                "/recipes/" + recipeId,
                Map.of("name", "Lazy sushi")
        );

        assertThat(patchResponse.get("name")).isEqualTo("Lazy sushi");
        assertThat(patchResponse.get("instructions")).isEqualTo("Call sushi bar");
    }

    @Test
    @Order(3)
    void reloadById_verifyNameUpdate() {
        Map byIdRecipe = testRestOperations.doGet("/recipes/" + recipeId);

        assert byIdRecipe.get("name").equals("Lazy sushi");
        assert byIdRecipe.get("instructions").equals("Call sushi bar");
    }

    @Test
    @Order(4)
    void deleteById_shouldRemoveFrom_getPage() {
        testRestOperations.doDelete("/recipes/" + recipeId);

        Map pageResponse = testRestOperations.doGet("/recipes");
        List<Map<String, Object>> content = (List) pageResponse.get("content");
        assert content.isEmpty();
    }

    @Test
    @Order(5)
    void getMissingRecipeById_shouldReturn404() {
        Map responseAsMap = testRestOperations.doGet(NOT_FOUND, "/recipes/" + recipeId);
        assert responseAsMap.get("message").equals("No recipe for id " + recipeId);
    }

}
