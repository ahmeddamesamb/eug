package sn.ugb.gateway.web.rest;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;
import static sn.ugb.gateway.test.util.OAuth2TestUtil.TEST_USER_LOGIN;
import static sn.ugb.gateway.test.util.OAuth2TestUtil.authenticationToken;
import static sn.ugb.gateway.test.util.OAuth2TestUtil.registerAuthenticationToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sn.ugb.gateway.IntegrationTest;
import sn.ugb.gateway.security.AuthoritiesConstants;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_TIMEOUT)
@IntegrationTest
class AccountResourceIT {

    private Map<String, Object> claims;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveOAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private ClientRegistration clientRegistration;

    @BeforeEach
    public void setup() {
        claims = new HashMap<>();
        claims.put("groups", Collections.singletonList(AuthoritiesConstants.ADMIN));
        claims.put("sub", "jane");
        claims.put("email", "jane.doe@jhipster.com");
    }

    @Test
    void testGetExistingAccount() {
        webTestClient
            .mutateWith(
                mockAuthentication(registerAuthenticationToken(authorizedClientService, clientRegistration, authenticationToken(claims)))
            )
            .mutateWith(csrf())
            .get()
            .uri("/api/account")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .jsonPath("$.login")
            .isEqualTo("jane")
            .jsonPath("$.email")
            .isEqualTo("jane.doe@jhipster.com")
            .jsonPath("$.authorities")
            .isEqualTo(AuthoritiesConstants.ADMIN);
    }

    @Test
    void testGetUnknownAccount() {
        webTestClient.get().uri("/api/account").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().is3xxRedirection();
    }

    @Test
    @WithUnauthenticatedMockUser
    void testNonAuthenticatedUser() {
        webTestClient
            .get()
            .uri("/api/authenticate")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .isEmpty();
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void testAuthenticatedUser() {
        webTestClient
            .get()
            .uri("/api/authenticate")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .isEqualTo(TEST_USER_LOGIN);
    }
}
