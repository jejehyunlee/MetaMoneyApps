package agile.metamoney.controller;

import agile.metamoney.model.request.LoginRequest;
import agile.metamoney.model.request.RegisterAdminRequest;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.*;
import agile.metamoney.service.AuthService;
import agile.metamoney.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @Test
    void testRegisterSuccess() throws Exception {
        RegisterCustomerRequest customerRequest = RegisterCustomerRequest.builder()
                .name("foo")
                .username("foo")
                .password("rahasia")
                .email("foo@mail.com")
                .nomerTelepon("1111")
                .build();

        RegisterCustomerResponse customerResponse = RegisterCustomerResponse.builder()
                .id("001TEST_ID")
                .username("foo")
                .nama("foo")
                .email("foo@mail.com")
                .nomerTelepon("1111")
                .build();

        when(authService.registerNewCustomer(any(RegisterCustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
        ).andExpectAll(
            status().isCreated()
        ).andDo(result -> {
            CommonResponse<RegisterCustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getId(), "001TEST_ID");
            assertEquals(response.getData().getUsername(), "foo");
            assertEquals(response.getData().getNama(), "foo");
            assertEquals(response.getData().getEmail(), "foo@mail.com");
            assertEquals(response.getData().getNomerTelepon(), "1111");

        });
        //verify
        verify(authService,times(1)).registerNewCustomer(any(RegisterCustomerRequest.class));
    }

    @Test
    void registerAdmin() throws Exception {
        RegisterAdminRequest adminRequestRequest = RegisterAdminRequest.builder()
                .nama("foo")
                .username("foo")
                .password("rahasia")
                .build();

        RegisterAdminResponse adminResponse = RegisterAdminResponse.builder()
                .id("001TEST_ID")
                .nama("foo")
                .username("foo")
                .build();

        when(authService.registerNewAdmin(any(RegisterAdminRequest.class))).thenReturn(adminResponse);

        mockMvc.perform(
                post("/api/v1/auth/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequestRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<RegisterAdminResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            System.out.println("TEST ID : " + response.getData().getId());
            assertNull(response.getErrors());
            assertEquals(response.getData().getId(), "001TEST_ID");
            assertEquals(response.getData().getUsername(), "foo");
            assertEquals(response.getData().getNama(), "foo");
        });

        verify(authService,times(1)).registerNewAdmin(any(RegisterAdminRequest.class));

    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("foo")
                .password("rahasia")
                .build();

        List<String> roles = List.of("ROLE_ADMIN");
        LoginResponse loginResponse = LoginResponse.builder()
                .username("foo")
                .roles(roles)
                .token("jwt_token_secret")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getUsername(), "foo");
            assertEquals(response.getData().getRoles().get(0), "ROLE_ADMIN");
            assertEquals(response.getData().getToken(), "jwt_token_secret");
        });

        verify(authService,times(1)).login(any(LoginRequest.class));

    }
}