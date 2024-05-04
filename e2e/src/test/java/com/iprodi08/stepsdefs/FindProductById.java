package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.VersionDto;
import com.iprodi08.stepsdefs.restclient.RestUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FindProductById {

    private String stringErrResponse;

    private ResponseEntity<ProductDto> responseProductEntity;

    private String endpoint;

    @Value("${base_url}")
    private String baseUrl;

    @Value("${app.version}")
    private String appVersion;

    @When("Product service is up and running")
    public void productServiceIsUpAndRunning()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //when

        String actualAppVersion;
        RestTemplate restTemplate = RestUtil.getRestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<VersionDto> responseEntity = restTemplate.getForEntity(
                baseUrl + "api/products/info",
                VersionDto.class
        );

        actualAppVersion = responseEntity.getBody().getAppVersion();

        //then
        assertThat(appVersion).isEqualTo(actualAppVersion);
    }

    @And("Product endpoint {string} with http method GET available")
    public void productEndpointWithHttpMethodGETAvailable(String endpointPart) {

        this.endpoint = endpointPart;

    }

    @When("client wants to find a product with id {long}")
    public void clientWantsFindProductWithId(long productId)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        RestTemplate restTemplate = RestUtil.getRestTemplate();

        try {
            responseProductEntity = restTemplate.getForEntity(
                    baseUrl + endpoint + productId,
                    ProductDto.class
            );
        } catch (HttpClientErrorException e) {
            stringErrResponse = e.getMessage();
        }
    }

    @Then("response code is {int}")
    public void responseCodeIs(int expectedCode) {
        final int actualCode;
        if (stringErrResponse != null) {
            String[] parseResponse = stringErrResponse.split(":");
            actualCode = Integer.parseInt(parseResponse[0].trim());
        } else {
            actualCode = responseProductEntity.getStatusCode().value();
        }
        assertThat(actualCode).isEqualTo(expectedCode);
    }

    @And("found product response body contains:")
    public void foundProductResponseBodyContains(DataTable table)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = RestUtil.getRestTemplate();

        //given
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        ProductDto expectedProduct = new ProductDto();

        for (Map<String, String> columns : rows) {

            expectedProduct = new ProductDto(
                    Long.parseLong(columns.get("id")),
                    columns.get("summary"),
                    columns.get("description"),
                    null,
                    null,
                    Boolean.parseBoolean(columns.get("active")),
                    null);
        }

        //when
        ProductDto actualProduct = restTemplate.getForEntity(
                baseUrl + "api/products/" + expectedProduct.getId(),
                ProductDto.class
        ).getBody();

        //then
        assert actualProduct != null;
        assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
        assertThat(actualProduct.getSummary()).isEqualTo(expectedProduct.getSummary());
        assertThat(actualProduct.getActive()).isEqualTo(expectedProduct.getActive());
        assertThat(actualProduct).isEqualTo(expectedProduct);

    }

}
