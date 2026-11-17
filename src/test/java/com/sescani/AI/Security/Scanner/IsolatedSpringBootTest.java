package com.sescani.AI.Security.Scanner;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/** Full application context with an ephemeral database and no external AI calls. */
@SpringBootTest
@TestPropertySource("classpath:integration-test.properties")
abstract class IsolatedSpringBootTest {

    @MockitoBean
    protected OllamaClient ollamaClient;

    @MockitoBean
    protected GroqClient groqClient;
}
