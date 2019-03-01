package org.auth1.auth1.api.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.auth1.auth1.core.authentication.RegistrationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = RegisterController.class)
public class RegisterServiceTest {
    private MockMvc mvc;

    @InjectMocks
    private RegisterController registerService;

    private JacksonTester<RegistrationResponse> json;

    private final String ENDPOINT = "/register";

    private final String VALID_USERNAME = "username";
    private final String VALID_EMAIL = "user@email.com";
    private final String VALID_PASSWORD = "password";

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(registerService).build();
    }

    @Test
    public void testRegister() throws Exception {
        RegistrationResponse expected = new RegistrationResponse(RegistrationResult.SERVICE_UNAVAILABLE);

        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD)
                                 .param("email", VALID_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(json.write(expected).getJson()));
    }

    @Test
    public void testRegisterWithMissingPassword() throws Exception {
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("email", VALID_EMAIL))
                .andExpect(status().isBadRequest());
    }
}