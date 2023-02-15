package com.example.cleaner_int_tests.test_utils;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Simplified test-version of org.springframework.web.client.RestOperations
 */
public interface TestRestOperations {

    /**
     * Accepts "raw" json content, expects 200 status and returns mockmvc result
     * actions for response assertions
     */
    ResultActions postActions(String path, String content);

    /**
     * Performs mockmvc-get, expects 200 status and reads response into Map / List object
     */
    <T> T doGet(String path);

    /**
     * Performs mockmvc-get, expects given status and parses response into object
     */
    <T> T doGet(HttpStatus expectedStatus, String path);

    <T> T doPost(String path, Object request);

    <T> T doPatch(String path, Object request);

    void doDelete(String path);
}
