package org.auth1.auth1.api.login;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.ExpiringToken;
import org.auth1.auth1.core.authentication.UserIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = AuthenticationController.class)
public class LoginTest {
    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private final String ENDPOINT = "/login";
    private final String VALID_USERNAME = "username";
    private final String VALID_EMAIL = "user@email.com";
    private final String VALID_PASSWORD = "password";
    private final String INVALID_PASSWORD = "badpass";
    private final ExpiringToken VALID_TOKEN = new ExpiringToken("foo", NOW);
    private MockMvc mvc;

    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationController authenticationController;
    private JacksonTester<LoginResponse> responseJson;
    private JacksonTester<ExpiringToken> tokenJson;

  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

      JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

      mvc = MockMvcBuilders.standaloneSetup(authenticationController)
          .setMessageConverters(springMvcJacksonConverter).build();
    }

    @Test
    public void loginWithoutTotp() throws Exception {
        AuthenticationResult loginResult = AuthenticationResult.forSuccess(VALID_TOKEN);
        when(this.authenticationManager.authenticate(UserIdentifier.forEmail(VALID_EMAIL),
                VALID_PASSWORD,null)).thenReturn(loginResult);
        when(this.authenticationManager.authenticate(UserIdentifier.forUsername(VALID_USERNAME),
                VALID_PASSWORD,null)).thenReturn(loginResult);
      when(
          this.authenticationManager.authenticate(UserIdentifier.forUsernameOrEmail(VALID_USERNAME),
              VALID_PASSWORD, null)).thenReturn(loginResult);
        when(this.authenticationManager.authenticate(UserIdentifier.forUsernameOrEmail(VALID_EMAIL),
                VALID_PASSWORD,null)).thenReturn(loginResult);

        LoginResponse expected = LoginResponse.fromAuthenticationResult(loginResult);

        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(responseJson.write(expected).getJson()));
        this.mvc.perform(post(ENDPOINT).param("email", VALID_EMAIL)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(responseJson.write(expected).getJson()));
        this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(responseJson.write(expected).getJson()));
        this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_EMAIL)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(responseJson.write(expected).getJson()));
    }

    @Test
    public void loginWithTooManyParams() throws Exception {
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("email", VALID_EMAIL)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isBadRequest());
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("usernameOrEmail", VALID_EMAIL)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isBadRequest());
        this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_USERNAME)
                                 .param("email", VALID_EMAIL)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isBadRequest());
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("email", VALID_EMAIL)
                                 .param("usernameOrEmail", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithMissingPassword() throws Exception {
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithFailedAuth() throws Exception {
        AuthenticationResult loginResult = AuthenticationResult.BAD_PASSWORD;
        when(this.authenticationManager.authenticate(UserIdentifier.forEmail(VALID_EMAIL), INVALID_PASSWORD, null))
                .thenReturn(loginResult);

        LoginResponse expected = LoginResponse.fromAuthenticationResult(loginResult);

        this.mvc.perform(post(ENDPOINT).param("email", VALID_EMAIL)
                                 .param("password", INVALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(responseJson.write(expected).getJson()));
    }
}
