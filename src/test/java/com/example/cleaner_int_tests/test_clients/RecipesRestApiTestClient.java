package com.example.cleaner_int_tests.test_clients;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface RecipesRestApiTestClient {

    Map getRecipesPage();

    Map createRecipe(Map request);

    Map getRecipe(String id);

    Map getRecipe(HttpStatus status, String id);

    Map updateRecipe(String id, Map request);

    void deleteRecipe(String id);

}
