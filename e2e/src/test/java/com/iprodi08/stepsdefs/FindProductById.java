package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.VersionDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FindProductById {

    private HttpResponse response;
    private String endpoint;

    @Value("${base_url}")
    private String baseUrl;

    @Value("${app.version}")
    private String appVersion;

    @When("Product service is up and running")
    public void productServiceIsUpAndRunning() {

        //when

        String actualAppVersion;
        RestClient restClient = RestClient.create();
        VersionDto versionDto = restClient.get()
                .uri(baseUrl + "/api/info")
                .retrieve()
                .body(VersionDto.class);

        actualAppVersion = versionDto != null ? versionDto.getAppVersion() : null;

        //then
        assertThat(appVersion).isEqualTo(actualAppVersion);
    }

    @And("Product endpoint {string} with http method GET available")
    public void productEndpointWithHttpMethodGETAvailable(String endpointPart) {

        this.endpoint = endpointPart;

    }

    @When("client wants to find a product with id {long}")
    public void clientWantsFindProductWithId(long productId) throws IOException, InterruptedException {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint + productId))
                    .GET()
                    .build();

            this.response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

    }

    @Then("response code is {int}")
    public void responseCodeIs(int expectedCode) {
        final int actualCode = response.statusCode();
        assertThat(actualCode).isEqualTo(expectedCode);
    }

    @And("found product response body contains:")
    public void foundProductResponseBodyContains(DataTable table) {

        //given
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        ProductDto expectedProduct = new ProductDto();

        for (Map<String, String> columns : rows) {

            expectedProduct = ProductDto.builder()
                    .id(Long.parseLong(columns.get("id")))
                    .summary(columns.get("summary"))
                    .active(columns.get("active").equals("true") ? Boolean.TRUE : Boolean.FALSE)
                    .description(columns.get("description"))
                    .build();
        }

        //when
        RestClient restClient = RestClient.create();
        ProductDto actualProduct = restClient.get()
                .uri(baseUrl + "api/products/" + expectedProduct.getId())
                .retrieve()
                .body(ProductDto.class);

        //then
        assertThat(actualProduct).isEqualTo(expectedProduct);

    }

}
