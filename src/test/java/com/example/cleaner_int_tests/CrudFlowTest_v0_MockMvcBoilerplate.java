package com.example.cleaner_int_tests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudFlowTest_v0_MockMvcBoilerplate extends BaseFullContextTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @Order(0)
    void createNewRecipe() throws Exception {
        mockMvc.perform(
                        post("/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "name" : "Man sushi",
                                        "instructions" : "Call sushi bar",
                                        "ingredients" : [ "Phone", "Sushi bar", "Money" ]
                                        }
                                        """
                                )
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        jsonPath("name").value("Man sushi")
                )
                .andExpect(
                        jsonPath("instructions").value("Call sushi bar")
                )
                .andExpect(
                        jsonPath("ingredients[0]").value("Phone")
                )
                .andExpect(
                        jsonPath("ingredients[1]").value("Sushi bar")
                )
                .andExpect(
                        jsonPath("ingredients[2]").value("Money")
                )
                .andReturn();
    }

    String recipeId;

    @Test
    @Order(1)
    void createdRecipe_shouldBeAvailable_viaGetPage_andViaGetById() throws Exception {
        String recipesPageResponseString = mockMvc.perform(
                        get("/recipes")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Map responseAsMap = OBJECT_MAPPER.readValue(recipesPageResponseString, Map.class);

        List<Map<String, Object>> content = (List) responseAsMap.get("content");
        assertThat(content).hasSize(1);
        var recipe = content.get(0);
        assertThat(recipe.get("name")).isEqualTo("Man sushi");
        assertThat(recipe.get("instructions")).isEqualTo("Call sushi bar");
        assertThat((List) recipe.get("ingredients"))
                .hasSize(3)
                .containsExactly("Phone", "Sushi bar", "Money");

        recipeId = (String) recipe.get("id");

        String byIdRecipeJson = mockMvc.perform(
                        get("/recipes/" + recipeId)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Map byIdRecipe = OBJECT_MAPPER.readValue(byIdRecipeJson, Map.class);
        assertThat(byIdRecipe).isEqualTo(recipe);
    }

    @Test
    @Order(2)
    void shouldUpdateRecipeById_inPatchManner() throws Exception {
        mockMvc.perform(
                        patch("/recipes/" + recipeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OBJECT_MAPPER.writeValueAsBytes(
                                        Map.of("name", "Lazy sushi")
                                ))
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        jsonPath("name").value("Lazy sushi")
                )
                // checking only instructions; by-design, remaining fields should also be ok
                .andExpect(
                        jsonPath("instructions").value("Call sushi bar")
                )
                .andReturn();
    }

    @Test
    @Order(3)
    void reloadById_verifyNameUpdate() throws Exception {
        String byIdRecipeJson = mockMvc.perform(
                        get("/recipes/" + recipeId)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map byIdRecipe = OBJECT_MAPPER.readValue(byIdRecipeJson, Map.class);
        assertThat(byIdRecipe.get("name")).isEqualTo("Lazy sushi");
        assertThat(byIdRecipe.get("instructions")).isEqualTo("Call sushi bar");
    }

    @Test
    @Order(4)
    void deleteById_shouldRemoveFrom_getPage() throws Exception {
        mockMvc.perform(
                delete("/recipes/" + recipeId)
        ).andExpect(status().isOk());

        String recipesPageResponseString = mockMvc.perform(
                        get("/recipes")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Map responseAsMap = OBJECT_MAPPER.readValue(recipesPageResponseString, Map.class);
        List<Map<String, Object>> content = (List) responseAsMap.get("content");
        assertThat(content).hasSize(0);
    }

    @Test
    @Order(5)
    void getMissingRecipeById_shouldReturn404() throws Exception {
        String notFoundResponse = mockMvc.perform(
                        get("/recipes/" + recipeId)
                ).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Map responseAsMap = OBJECT_MAPPER.readValue(notFoundResponse, Map.class);
        assertThat(responseAsMap.get("message")).isEqualTo("No recipe for id " + recipeId);
    }

}
