package com.example.cleaner_int_tests

import com.example.apiclient.api.RecipesControllerApi
import com.example.apiclient.invoker.ApiClient
import com.example.apiclient.model.RecipeRequest
import com.example.cleaner_int_tests.data.RecipeEntity
import com.example.cleaner_int_tests.data.RecipeEntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.argThat
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@WebMvcTest
@ContextConfiguration(classes = MockMvcShallowTestContext)
class ShallowControllerTest_WithGeneratedClient {

    @Autowired
    RecipesControllerApi apiClient

    @Autowired
    RecipeEntityRepository mockRepository

    @Test
    void createNewRecipe_willCallRepoAndMakeAllConversions() {
        when(mockRepository.save(argThat({ RecipeEntity it ->
            assert it.name == "Man sushi"
            assert it.instructions == "Call sushi bar"
            assert it.getIngredientsList() == ["Phone", "Sushi bar", "Money"]
            return true
        }))).thenReturn(new RecipeEntity(
                "123", "Man sushi", "Call sushi bar",
                "Phone,Sushi bar,Money"
        ))

        def response = apiClient.createRecipe(new RecipeRequest()
                .name("Man sushi").instructions("Call sushi bar")
                .ingredients(["Phone", "Sushi bar", "Money"])
        )

        assert response.name == "Man sushi"
        assert response.instructions == "Call sushi bar"
        assert response.ingredients == ["Phone", "Sushi bar", "Money"]

        verify(mockRepository).save(any())
    }

    @ComponentScan([
            "com.example.cleaner_int_tests.web",
            "com.example.apiclient.api"
    ])
    static class MockMvcShallowTestContext {

        @MockBean
        RecipeEntityRepository mockRepository

        @Bean
        ApiClient apiClient(MockMvc mockMvc) {
            RestTemplate restTemplate = new RestTemplate(
                    new MockMvcClientHttpRequestFactory(mockMvc)
            );
            return new ApiClient(restTemplate)
        }

    }

}
