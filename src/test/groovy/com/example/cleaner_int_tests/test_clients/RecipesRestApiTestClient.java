package com.example.cleaner_int_tests.test_clients;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface RecipesRestApiTestClient {

    Map getRecipesPage();

    /**
     *
     * Sample request:
     * { "name" : "Man sushi", ... }
     *
     * Sample response:
     * { "id" : "random-uuid", "name" : "Man sushi", ... }
     */
    Map createRecipe(Map request);

    Map getRecipe(String id);

    /** If non-200 status is expected */
    Map getRecipe(HttpStatus status, String id);

    Map updateRecipe(String id, Map request);

    void deleteRecipe(String id);

}
