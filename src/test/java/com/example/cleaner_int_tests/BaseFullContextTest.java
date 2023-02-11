package com.example.cleaner_int_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_CLASS)
@ActiveProfiles("test")
public class BaseFullContextTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

}
