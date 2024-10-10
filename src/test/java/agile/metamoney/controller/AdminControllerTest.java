package agile.metamoney.controller;

import agile.metamoney.entity.Admin;
import agile.metamoney.model.request.RegisterCustomerRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.RegisterCustomerResponse;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @Test
    void getAdminById() throws Exception {
        Admin admin = Admin.builder()
                .id("idAdmin")
                .nama("admin")
                .build();
        when(adminService.getById(anyString())).thenReturn(admin);

        mockMvc.perform(
                get("/api/v1/admin/idAdmin")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<Admin> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getId(), "idAdmin");
            assertEquals(response.getData().getNama(), "admin");
        });
        //verify
        verify(adminService,times(1)).getById(any(String.class));
    }

    @Test
    void getAllCustomer() throws Exception {
        Admin admin1 = Admin.builder()
                .id("idAdmin")
                .nama("admin1")
                .build();
        Admin admin2 = Admin.builder()
                .id("idAdmin")
                .nama("admin2")
                .build();
        List<Admin> admins = new ArrayList<>();
        admins.add(admin1);
        admins.add(admin2);

        when(adminService.getAll(anyString())).thenReturn(admins);

        mockMvc.perform(
                get("/api/v1/admin/idAdmin/all-admin")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<Admin>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getNama(), "admin1");
            assertEquals(response.getData().get(1).getNama(), "admin2");
        });
        //verify
        verify(adminService,times(1)).getAll(any(String.class));
    }

    @Test
    void deleteAdmin() throws Exception {
        mockMvc.perform(
                delete("/api/v1/admin/id-deleteIdAdmin/current-id-idAdmin")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
        //verify
        verify(adminService,times(1)).deleteById(anyString(),anyString());
    }
}