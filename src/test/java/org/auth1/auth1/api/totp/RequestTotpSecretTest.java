package org.auth1.auth1.api.totp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.CreateTentativeTOTPResult;
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
public class RequestTotpSecretTest {

  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private final String ENDPOINT = "/requestTotpSecret";
  private final String VALID_TOKEN = "foo";
  private final byte[] VALID_SECRET = "foo123".getBytes();

  private MockMvc mvc;

  @Mock
  private AuthenticationManager authenticationManager;
  @InjectMocks
  private TotpController controller;
  private JacksonTester<RequestTotpSecretResponse> json;

  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

    mvc = MockMvcBuilders.standaloneSetup(controller)
        .setMessageConverters(springMvcJacksonConverter).build();
  }

  @Test
  public void requestTotpWithToken() throws Exception {
    CreateTentativeTOTPResult result = CreateTentativeTOTPResult.forSuccess(VALID_SECRET, NOW);
    when(this.authenticationManager.createTentativeTOTPSecret(VALID_TOKEN)).thenReturn(result);

    RequestTotpSecretResponse expected = RequestTotpSecretResponse.fromResult(result);

    this.mvc.perform(post(ENDPOINT).param("token", VALID_TOKEN)).andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json(json.write(expected).getJson()));
  }

  @Test
  public void requestTotpFailsWhenMissingToken() throws Exception {
    this.mvc.perform(post(ENDPOINT)).andExpect(status().isBadRequest());
  }
}