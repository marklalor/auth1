package org.auth1.auth1.api.totp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.ConfirmTentativeTOTPResult;
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
@SpringBootTest(classes = TotpController.class)
public class ValidateTotpSecretTest {

  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private final String ENDPOINT = "/validateTotpSecret";
  private final String VALID_TOKEN = "foo";
  private final String VALID_CODE = "123456";
  private final byte[] VALID_SECRET = "foo123".getBytes();

  private MockMvc mvc;

  @Mock
  private AuthenticationManager authenticationManager;
  @InjectMocks
  private TotpController controller;
  private JacksonTester<ValidateTotpSecretResponse> json;

  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

    mvc = MockMvcBuilders.standaloneSetup(controller)
        .setMessageConverters(springMvcJacksonConverter).build();
  }

  @Test
  public void validateTotpWithTokenAndCode() throws Exception {
    ConfirmTentativeTOTPResult result = ConfirmTentativeTOTPResult.SUCCESS;
    when(this.authenticationManager.confirmTentativeTOTPSecret(VALID_TOKEN, VALID_CODE))
        .thenReturn(result);

    ValidateTotpSecretResponse expected = ValidateTotpSecretResponse.fromResult(result);

    this.mvc.perform(post(ENDPOINT).param("token", VALID_TOKEN).param("code", VALID_CODE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
  }

  @Test
  public void validateTotpFailsWhenMissingTokenOrCode() throws Exception {
    this.mvc.perform(post(ENDPOINT)).andExpect(status().isBadRequest());
    this.mvc.perform(post(ENDPOINT).param("token", VALID_TOKEN)).andExpect(status().isBadRequest());
    this.mvc.perform(post(ENDPOINT).param("code", VALID_CODE)).andExpect(status().isBadRequest());
  }
}