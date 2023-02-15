package com.example.cleaner_int_tests.test_clients;

import com.example.cleaner_int_tests.test_utils.TestRestOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
class RecipesRestApiTestClientImpl implements RecipesRestApiTestClient {

    private final TestRestOperations restOperations;

    @Override
    public Map getRecipesPage() {
        return restOperations.doGet("/recipes");
    }

    @Override
    public Map createRecipe(Map request) {
        return restOperations.doPost("/recipes", request);
    }

    @Override
    public Map getRecipe(String id) {
        return restOperations.doGet("/recipes/" + id);
    }

    @Override
    public Map getRecipe(HttpStatus status, String id) {
        return restOperations.doGet(status, "/recipes/" + id);
    }

    @Override
    public Map updateRecipe(String id, Map request) {
        return restOperations.doPatch("/recipes/" + id, request);
    }

    @Override
    public void deleteRecipe(String id) {
        restOperations.doDelete("/recipes/" + id);
    }
}
