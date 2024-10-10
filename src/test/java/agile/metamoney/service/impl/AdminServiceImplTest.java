package agile.metamoney.service.impl;

import agile.metamoney.entity.Admin;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceImplTest {
    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final ValidationService validationService = mock(ValidationService.class);

    private final AdminService adminService = new AdminServiceImpl(adminRepository, validationService);

    @Test
    void getById() {
        Admin admin = Admin.builder()
                .id("idAdmin")
                .nama("admin")
                .build();
        when(adminRepository.findById(anyString())).thenReturn(Optional.ofNullable(admin));

        Admin findAdmin = adminService.getById("test");
        verify(adminRepository, times(1)).findById(anyString());
        assertEquals(findAdmin.getId(), "idAdmin");
        assertEquals(findAdmin.getNama(), "admin");
    }

    @Test
    void getAll() {
        Admin admin1 = Admin.builder()
                .id("idAdmin")
                .nama("admin1")
                .build();
        Admin admin2 = Admin.builder()
                .id("idAdmin2")
                .nama("admin2")
                .build();
        List<Admin> admins = new ArrayList<>();
        admins.add(admin1);
        admins.add(admin2);

        when(adminRepository.findById(anyString())).thenReturn(Optional.ofNullable(admin1));
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> listAdmin = adminService.getAll("test");

        verify(adminRepository, times(1)).findById(anyString());
        verify(adminRepository, times(1)).findAll();

        assertNotNull(listAdmin);
        assertEquals(listAdmin.size(), 2);
        assertEquals(listAdmin.get(0).getId(), "idAdmin");
        assertEquals(listAdmin.get(0).getNama(), "admin1");
        assertEquals(listAdmin.get(1).getId(), "idAdmin2");
        assertEquals(listAdmin.get(1).getNama(), "admin2");
    }

    @Test
    void deleteById() {
        Admin admin = Admin.builder()
                .id("idAdmin")
                .nama("admin")
                .build();
        when(adminRepository.findById(anyString())).thenReturn(Optional.ofNullable(admin));

        adminService.deleteById("test", "idtest");
        verify(adminRepository, times(2)).findById(anyString());
        verify(adminRepository, times(1)).deleteById(anyString());

    }
}