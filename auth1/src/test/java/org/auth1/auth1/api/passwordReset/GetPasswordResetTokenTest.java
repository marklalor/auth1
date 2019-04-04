package org.auth1.auth1.api.passwordReset;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.ExpiringToken;
import org.auth1.auth1.core.authentication.GeneratePasswordResetTokenResult;
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
@SpringBootTest(classes = PasswordResetController.class)
public class GetPasswordResetTokenTest {

  private final String ENDPOINT = "/getPasswordResetToken";
  private final ZonedDateTime NOW = ZonedDateTime.now();
  private final String VALID_USERNAME = "user";
  private final String INVALID_USERNAME = "nonuser";
  private final String VALID_EMAIL = "user@email.com";
  private final ExpiringToken VALID_TOKEN = new ExpiringToken("foo", NOW);
  private MockMvc mvc;
  @Mock
  private AuthenticationManager authenticationManager;
  @InjectMocks
  private PasswordResetController passwordResetController;
  private JacksonTester<GetPasswordResetTokenResponse> json;
  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

    mvc = MockMvcBuilders.standaloneSetup(passwordResetController)
        .setMessageConverters(springMvcJacksonConverter).build();
  }

  @Test
  public void getTokenSuccess() throws Exception {
    GeneratePasswordResetTokenResult generateTokenResult = GeneratePasswordResetTokenResult
        .forSuccess(VALID_TOKEN);
    when(this.authenticationManager
        .generatePasswordResetToken(UserIdentifier.forUsername(VALID_USERNAME)))
        .thenReturn(generateTokenResult);
    when(this.authenticationManager
        .generatePasswordResetToken(UserIdentifier.forEmail(VALID_EMAIL)))
        .thenReturn(generateTokenResult);
    when(this.authenticationManager
        .generatePasswordResetToken(UserIdentifier.forUsernameOrEmail(VALID_USERNAME)))
        .thenReturn(generateTokenResult);
    when(this.authenticationManager
        .generatePasswordResetToken(UserIdentifier.forUsernameOrEmail(VALID_EMAIL)))
        .thenReturn(generateTokenResult);

    GetPasswordResetTokenResponse expected = GetPasswordResetTokenResponse
        .fromResult(generateTokenResult);

    this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
    this.mvc.perform(post(ENDPOINT).param("email", VALID_EMAIL))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
    this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_USERNAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
    this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_EMAIL))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
  }

  @Test
  public void getTokenWithTooManyParams() throws Exception {
    this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
        .param("email", VALID_EMAIL))
        .andExpect(status().isBadRequest());
    this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
        .param("usernameOrEmail", VALID_EMAIL))
        .andExpect(status().isBadRequest());
    this.mvc.perform(post(ENDPOINT).param("usernameOrEmail", VALID_USERNAME)
        .param("email", VALID_EMAIL))
        .andExpect(status().isBadRequest());
    this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
        .param("email", VALID_EMAIL)
        .param("usernameOrEmail", VALID_USERNAME))
        .andExpect(status().isBadRequest());

  }

  @Test
  public void getTokenWithNonexistentAccount() throws Exception {
    GeneratePasswordResetTokenResult generatePasswordResetTokenResult = GeneratePasswordResetTokenResult.ACCOUNT_DOES_NOT_EXIST;
    when(this.authenticationManager
        .generatePasswordResetToken(UserIdentifier.forUsername(INVALID_USERNAME)))
        .thenReturn(generatePasswordResetTokenResult);

    GetPasswordResetTokenResponse expected = GetPasswordResetTokenResponse
        .fromResult(generatePasswordResetTokenResult);

    this.mvc.perform(post(ENDPOINT).param("username", INVALID_USERNAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
  }
}