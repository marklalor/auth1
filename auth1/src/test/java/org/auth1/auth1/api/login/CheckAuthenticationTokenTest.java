package org.auth1.auth1.api.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.CheckAuthenticationTokenResult;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = AuthenticationController.class)
public class CheckAuthenticationTokenTest {
    private final String ENDPOINT = "/checkAuthToken";
    private final String VALID_TOKEN = "foo";
    private final String INVALID_TOKEN = "bar";
    private final int USER_ID = 123;
    private MockMvc mvc;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationController authenticationController;
    private JacksonTester<CheckAuthenticationTokenResponse> json;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void checkValidToken() throws Exception {
        CheckAuthenticationTokenResult checkTokenResult = CheckAuthenticationTokenResult.forSuccess(USER_ID);
        when(this.authenticationManager.checkAuthenticationToken(VALID_TOKEN)).thenReturn(checkTokenResult);

        CheckAuthenticationTokenResponse expected = CheckAuthenticationTokenResponse.fromResult(checkTokenResult);

        this.mvc.perform(post(ENDPOINT).param("token", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(json.write(expected).getJson()));
    }

    @Test
    public void checkInvalidToken() throws Exception {
        CheckAuthenticationTokenResult checkTokenResult = CheckAuthenticationTokenResult.forSuccess(USER_ID);
        when(this.authenticationManager.checkAuthenticationToken(INVALID_TOKEN)).thenReturn(checkTokenResult);

        CheckAuthenticationTokenResponse expected = CheckAuthenticationTokenResponse.fromResult(checkTokenResult);

        this.mvc.perform(post(ENDPOINT).param("token", INVALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(json.write(expected).getJson()));
    }

    @Test
    public void checkMissingToken() throws Exception {
        this.mvc.perform(post(ENDPOINT))
                .andExpect(status().isBadRequest());
    }
}