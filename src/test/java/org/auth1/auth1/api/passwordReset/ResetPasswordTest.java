package org.auth1.auth1.api.passwordReset;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.ResetPasswordResult;
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
public class ResetPasswordTest {

  private final String ENDPOINT = "/resetPassword";
  private final ZonedDateTime NOW = ZonedDateTime.now();
  private final String VALID_PASSWORD = "password";
  private final String INVALID_PASSWORD = "ji32k7au4a83";
  private final String VALID_TOKEN = "foo";
  private final String INVALID_TOKEN = "bar";

  private MockMvc mvc;
  @Mock
  private AuthenticationManager authenticationManager;
  @InjectMocks
  private PasswordResetController passwordResetController;
  private JacksonTester<ResetPasswordResponse> json;
  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

    mvc = MockMvcBuilders.standaloneSetup(passwordResetController)
        .setMessageConverters(springMvcJacksonConverter).build();
  }

  @Test
  public void resetPasswordSuccess() throws Exception {
    ResetPasswordResult resetPasswordResult = ResetPasswordResult.SUCCESS;
    when(this.authenticationManager.resetPassword(VALID_TOKEN, VALID_PASSWORD))
        .thenReturn(resetPasswordResult);

    ResetPasswordResponse expected = ResetPasswordResponse.fromResult(resetPasswordResult);

    this.mvc
        .perform(post(ENDPOINT).param("token", VALID_TOKEN).param("newPassword", VALID_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
  }
}