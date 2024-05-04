package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.stepsdefs.restclient.RestUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
    public void whenRequestAllProducts() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        RestTemplate restTemplate = RestUtil.getRestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
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
        final int expectedSize = 5;
        List<ProductDto> productDtos = responseEntity.getBody();
        assertThat(productDtos).hasSize(expectedSize);
        assertEquals(responseEntity.getStatusCode().value(), statusCode);
    }
}
