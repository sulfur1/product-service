package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.VersionDto;
import com.iprodi08.productservice.restclient.ProductRestClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FindProductById {

    private String stringErrResponse;

    private ResponseEntity<ProductDto> responseProductEntity;

    private String endpoint;

    private ProductRestClient client;

    @Value("${base_url}")
    private String baseUrl;

    @Value("${app.version}")
    private String appVersion;

    @When("Product service is up and running")
    public void productServiceIsUpAndRunning() {
        //when

        String actualAppVersion;

        client = ProductRestClient
                .builder()
                .enableHttpsWithIgnoreSelfSignCertificate(true)
                .url(" ")
                .build();

        ResponseEntity<VersionDto> responseEntity = client.getRestTemplate().getForEntity(
                baseUrl + "api/v1/products/info",
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
    public void clientWantsFindProductWithId(long productId) {
        client.setUrl(baseUrl + endpoint + productId);

        try {
            responseProductEntity = client.getProduct();
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
    public void foundProductResponseBodyContains(DataTable table) {

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
        client.setUrl(baseUrl + "api/v1/products/" + expectedProduct.getId());

        ProductDto actualProduct = client.getProduct().getBody();

        //then
        assert actualProduct != null;
        assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
        assertThat(actualProduct.getSummary()).isEqualTo(expectedProduct.getSummary());
        assertThat(actualProduct.getActive()).isEqualTo(expectedProduct.getActive());
        assertThat(actualProduct).isEqualTo(expectedProduct);

    }

}
