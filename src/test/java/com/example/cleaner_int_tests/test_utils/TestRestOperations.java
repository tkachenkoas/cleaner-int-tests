package com.example.cleaner_int_tests.test_utils;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Simplified test-version of org.springframework.web.client.RestOperations
 */
public interface TestRestOperations {

    ResultActions postActions(String path, String content);

    <T> T doGet(String path);

    <T> T doPost(String path, Object request);

    <T> T doGet(HttpStatus expectedStatus, String path);

    <T> T doPatch(String path, Object request);

    void doDelete(String path);
}
