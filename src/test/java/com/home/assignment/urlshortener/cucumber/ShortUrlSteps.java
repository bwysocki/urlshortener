package com.home.assignment.urlshortener.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortUrlSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;
    private String requestBody;

    @Given("the following URL shorten request:")
    public void givenTheFollowingRequest(String requestBody) {
        this.requestBody = requestBody;
    }

    @When("I send a {string} request to {string}")
    public void whenISendARequestTo(String method, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        response = restTemplate.exchange(path, HttpMethod.valueOf(method.toUpperCase()), requestEntity, String.class);
    }

    @Then("the response status should be {int}")
    public void thenTheResponseStatusShouldBe(int status) {
        assertThat(response.getStatusCodeValue()).isEqualTo(status);
    }

    @Then("the response JSON should contain:")
    public void thenTheResponseJsonShouldContain(String expectedJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualJson = objectMapper.readTree(response.getBody());
        JsonNode expectedJsonNode = objectMapper.readTree(expectedJson);

        assertThat(actualJson).isEqualTo(expectedJsonNode);
    }
}
