package com.healthtracker.healthtracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    int port;

    HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    String url(String path) {
        return "http://localhost:"+port+path;
    }

    @Test
    void register_then_login_ok() {
        RestTemplate client = new RestTemplate();

        String regBody = "{\"username\":\"alice\",\"password\":\"pass\"}";
        ResponseEntity<String> reg = client.exchange(
                url("/users/register"),
                HttpMethod.POST,
                new HttpEntity<>(regBody, jsonHeaders()),
                String.class
        );
        assertThat(reg.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reg.getBody()).contains("registered");

        String loginBody = "{\"username\":\"alice\",\"password\":\"pass\"}";
        ResponseEntity<String> login = client.exchange(
                url("/users/login"),
                HttpMethod.POST,
                new HttpEntity<>(loginBody,jsonHeaders()),
                String.class
        );
        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(login.getBody()).contains("\"status\":\"login_ok\"");
        assertThat(login.getBody()).contains("\"id\":");
    }
}
