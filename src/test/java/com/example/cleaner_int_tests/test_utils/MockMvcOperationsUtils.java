package com.example.cleaner_int_tests.test_utils;

import lombok.SneakyThrows;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Contains various helper methods that help reduce amount of copy-paste
 */
public class MockMvcOperationsUtils {

    @SneakyThrows
    public static void asserJsonPaths(
            ResultActions resultActions,
            Map<String, String> pathValues
    ) {
        for (Map.Entry<String, String> entry : pathValues.entrySet()) {
            resultActions.andExpect(
                    jsonPath(entry.getKey()).value(entry.getValue())
            );
        }
    }


}
