package com.example.cleaner_int_tests.test_utils;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.example.cleaner_int_tests.BaseFullContextTest.OBJECT_MAPPER;

public class ApiClientUtils {

    record FailureResponse(String message) {

    }

    @SneakyThrows
    public static FailureResponse expectHttpFailure(
            HttpStatus status, Runnable operation
    ) {
        try {
            operation.run();
            throw new RuntimeException("Code is expected to throw HttpStatusCodeException with status " + status);
        } catch (HttpStatusCodeException e) {
            assert e.getStatusCode() == status;
            return OBJECT_MAPPER.readValue(e.getResponseBodyAsString(), FailureResponse.class);
        }
    }

}
