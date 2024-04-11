package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource
public class GetAllProducts {

    @Value("${base_url}")
    private String baseUrl;

    private ResponseEntity<List<ProductDto>> responseEntity;

    private String endpoint;

    @Given("the endpoint for getting all products {string} with the http GET method is given")
    public void givenEndPoint(String endPoint) {
        this.endpoint = endPoint;
    }

    @When("client want to get all products")
    public void whenRequestAllProducts() {
        RestTemplate restTemplate = new RestTemplate();
        responseEntity = restTemplate.exchange(
                baseUrl + endpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {

                }
        );
    }

    @Then("response status is {int}")
    public void thenReturnListOfProductWithStatusOk(Integer statusCode) {
        List<ProductDto> productDtos = responseEntity.getBody();
        assertThat(productDtos).isEmpty();
        assertEquals(responseEntity.getStatusCode().value(), statusCode);
    }
}
