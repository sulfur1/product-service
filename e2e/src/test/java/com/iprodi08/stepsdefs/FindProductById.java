package com.iprodi08.stepsdefs;

import com.iprodi08.productservice.dto.VersionDto;
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

        actualAppVersion = versionDto.getAppVersion();

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

}
