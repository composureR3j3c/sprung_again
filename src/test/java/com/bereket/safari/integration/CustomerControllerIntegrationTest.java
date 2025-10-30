package com.bereket.safari.integration;

import com.bereket.safari.dto.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    void authenticate() {
        // ---------- REGISTER ADMIN ----------
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> registerPayload = Map.of(
            "username", "bereket",
            "password", "12345"
        );

        HttpEntity<Map<String, String>> registerRequest = new HttpEntity<>(registerPayload, headers);

        ResponseEntity<String> registerResponse = restTemplate.exchange(
            "/api/v1/auth/registerAdmin",
            HttpMethod.POST,
            registerRequest,
            String.class
        );

        // If user already exists, OK or CONFLICT is acceptable
        assertThat(registerResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // ---------- LOGIN ----------
        Map<String, String> loginPayload = Map.of(
            "username", "bereket",
            "password", "12345"
        );

        HttpEntity<Map<String, String>> loginRequest = new HttpEntity<>(loginPayload, headers);

        ResponseEntity<String> loginResponse = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            loginRequest,
             String.class   
        );
            System.out.println("=== LOGIN RESPONSE ===");
            System.out.println("Status: " + loginResponse.getStatusCode());
            System.out.println("Headers: " + loginResponse.getHeaders());
            System.out.println("Body: " + loginResponse.getBody());
            System.out.println("======================");


        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        // assertThat(loginResponse.getBody()).containsKey("token");

        jwtToken = (String) loginResponse.getBody();
        // jwtToken = (String) loginResponse.getBody().get("token");
        // assertThat(jwtToken).isNotBlank();
    }

    @Test
    void testCreateAndGetCustomer() {
        // ---------- CREATE CUSTOMER ----------
        CustomerDto newCustomer = new CustomerDto(null, "John Doe", "john@example.com", "123456789");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CustomerDto> createRequest = new HttpEntity<>(newCustomer, headers);

        ResponseEntity<CustomerDto> createResponse = restTemplate.exchange(
                "/api/v1/customers",
                HttpMethod.POST,
                createRequest,
                CustomerDto.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CustomerDto created = createResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getEmail()).isEqualTo("john@example.com");

        // ---------- GET CUSTOMER ----------
        ResponseEntity<CustomerDto> getResponse = restTemplate.exchange(
                "/api/v1/customers/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CustomerDto.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getName()).isEqualTo("John Doe");
    }
}
