package org.auth1.auth1.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.api.login.LoginResponse;
import org.auth1.auth1.config.IntegrationTestConfig;
import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.ExpiringToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
//@AutoConfigureWebMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class})
@WebAppConfiguration
public class LoginIntegrationTest {

  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private final String ENDPOINT = "/login";
  private final String VALID_USERNAME = "username";
  private final String VALID_EMAIL = "user@email.com";
  private final String VALID_PASSWORD = "password";
  private final String INVALID_PASSWORD = "badpass";
  private final ExpiringToken VALID_TOKEN = new ExpiringToken("foo", NOW);


  private JacksonTester<LoginResponse> responseJson;

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext wac;

  private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();


  @Before
  public void setUp() {
    JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());

    mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void loginWithoutTotp() throws Exception {
    AuthenticationResult loginResult = AuthenticationResult.forSuccess(VALID_TOKEN);
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
}
