package org.auth1.auth1.api.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.RegistrationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = RegisterController.class)
public class RegisterControllerTest {
    private MockMvc mvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private RegisterController registerController;

    private JacksonTester<RegistrationResponse> json;

    private final String ENDPOINT = "/saveUser";

    private final String VALID_USERNAME = "username";
    private final String VALID_EMAIL = "user@email.com";
    private final String DUPLICATE_EMAIL = VALID_EMAIL;
    private final String VALID_PASSWORD = "password";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    public void testSuccessfulRegister() throws Exception {
        when(this.authenticationManager.register(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD))
                .thenReturn(RegistrationResult.SUCCESS);

        RegistrationResponse expected = new RegistrationResponse(RegistrationResult.SUCCESS);

        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD)
                                 .param("email", VALID_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(json.write(expected).getJson()));
    }

    @Test
    public void registerWithDuplicateEmail() throws Exception {
        given(this.authenticationManager.register(VALID_USERNAME, DUPLICATE_EMAIL, VALID_PASSWORD))
                .willReturn(RegistrationResult.EMAIL_DUPLICATE);

        RegistrationResponse expected = new RegistrationResponse(RegistrationResult.EMAIL_DUPLICATE);

        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("password", VALID_PASSWORD)
                                 .param("email", DUPLICATE_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(json.write(expected).getJson()));
    }

    @Test
    public void registerWithMissingPassword() throws Exception {
        this.mvc.perform(post(ENDPOINT).param("username", VALID_USERNAME)
                                 .param("email", VALID_EMAIL))
                .andExpect(status().isBadRequest());
    }
}