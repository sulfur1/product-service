package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.restclient.ProductRestClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

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
        ProductRestClient client = ProductRestClient
                .builder()
                .enableHttpsWithIgnoreSelfSignCertificate(true)
                .url(baseUrl + endpoint)
                .build();
        responseEntity = client.getListProducts();
    }

    @Then("response status is {int}")
    public void thenReturnListOfProductWithStatusOk(int expectedCode) {
        final int expectedSize = 5;
        List<ProductDto> productDtos = responseEntity.getBody();
        assertThat(productDtos).hasSize(expectedSize);
        assertEquals(responseEntity.getStatusCode().value(), expectedCode);
    }
}
