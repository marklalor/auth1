package org.auth1.auth1.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import org.auth1.auth1.Application;
import org.auth1.auth1.api.login.LoginResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class PasswordResetIntegrationTest {

    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private final String REGISTER_ENDPOINT = "/register";
    private final String GET_TOKEN_ENDPOINT = "/getPasswordResetToken";
    private final String RESET_PASS_ENDPOINT = "/resetPassword";
    private final String VALID_USERNAME = "username";
    private final String VALID_EMAIL = "user@email.com";
    private final String VALID_PASSWORD = "password";
    private final String NEW_VALID_PASSWORD = "newPassword";

    private JacksonTester<LoginResponse> responseJson;

    @Autowired
    private MockMvc mvc;

    private MappingJackson2HttpMessageConverter springMvcJacksonConverter = new MappingJackson2HttpMessageConverter();

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, springMvcJacksonConverter.getObjectMapper());
        this.mvc.perform(post("/register").param("username", VALID_USERNAME)
                .param("email", VALID_EMAIL)
                .param("password", VALID_PASSWORD));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void resetPassword() throws Exception {
        final MvcResult mvcResult = this.mvc.perform(post(GET_TOKEN_ENDPOINT).param("username", VALID_USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        // TODO: Brian is stuck on how to get the token from mvcResult

//        this.mvc.perform(post(RESET_PASS_ENDPOINT).param("token", mvcResult.getResponse().)
//                .param("newPassword", NEW_VALID_PASSWORD))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn();
    }
}
