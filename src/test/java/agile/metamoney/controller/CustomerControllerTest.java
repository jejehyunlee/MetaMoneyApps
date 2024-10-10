package agile.metamoney.controller;

import agile.metamoney.entity.Admin;
import agile.metamoney.entity.Customer;
import agile.metamoney.entity.constant.EGender;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.request.UpdateCustomerWorkRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.CustomerResponse;
import agile.metamoney.model.response.CustomerWorkResponse;
import agile.metamoney.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void itShouldReturnCustomerWhenUpdateCustomer() throws Exception {
        UpdateCustomerRequest updateCustomerRequest = UpdateCustomerRequest.builder()
                .id("1")
                .tanggalLahir("2013-10-01")
                .jenisKelamin("PRIA")
                .nik("3273173505980002")
                .namaBank("BCA")
                .noRek("5xx-13xx-xxx")
                .email("Chr@gmail.com")
                .nomerTelepon("082812829108")
                .kota("Bandung")
                .kecamatan("Bojongsoang")
                .rt("01")
                .rw("01")
                .alamatLengkap("Jl. Susah Sempit No.XX")
                .statusHubungan("Ibu")
                .namaKondar("Santi")
                .isActive(true)
                .noTelpkondar("082812829121")
                .customerWork(UpdateCustomerWorkRequest.builder()
                        .tipePekerjaan("pengusaha")
                        .bidangPekerjaan("Perbankan")
                        .namaPekerjaan("Credit Analyst")
                        .namaPerusahaan("PT. Samsat")
                        .nomerTelepon("0225220435")
                        .build())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(updateCustomerRequest);

        MockMultipartFile fileKtp = new MockMultipartFile("ftKtp","image.jpg", MediaType.IMAGE_JPEG_VALUE,"ktp-image-content".getBytes());
        MockMultipartFile fileDiri = new MockMultipartFile("ftDiri","image.jpg", MediaType.IMAGE_JPEG_VALUE,"diri-image-content".getBytes());

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id("1")
                .name("Sawas")
                .tanggalLahir(LocalDate.of(2013,10,1))
                .jenisKelamin(EGender.PRIA)
                .nik("3273173505980002")
                .fotoKtp("uploads/image.jpg")
                .fotoDiri("uploads/image.jpg")
                .namaBank("BCA")
                .noRek("5xx-13xx-xxx")
                .email("Chr@gmail.com")
                .nomerTelepon("082812829108")
                .kota("Bandung")
                .kecamatan("Bojongsoang")
                .rt("01")
                .rw("01")
                .isActive(true)
                .alamatLengkap("Jl. Susah Sempit No.XX")
                .statusHubungan("Ibu")
                .namaKondar("Santi")
                .isActive(true)
                .noTelpkondar("082812829121")
                .workResponse(CustomerWorkResponse.builder()
                        .tipePekerjaan("pengusaha")
                        .bidangPekerjaan("Perbankan")
                        .namaPekerjaan("Credit Analyst")
                        .namaPerusahaan("PT. Samsat")
                        .nomerTelepon("0225220435")
                        .build())
                .build();

        MockPart update_json= new MockPart("update-customer",jsonRequest.getBytes());

        when(customerService.updateCustomer(any(UpdateCustomerRequest.class),any(MultipartFile.class),any(MultipartFile.class))).thenReturn(customerResponse);

        mockMvc.perform(multipart("/api/v1/customer")
                        .file(fileKtp)
                        .file(fileDiri)
                        .part(update_json)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(response.getData().getId(), "1");
                    assertEquals(response.getData().getName(), "Sawas");

                });
        verify(customerService,times(1)).updateCustomer(any(UpdateCustomerRequest.class),any(MultipartFile.class),any(MultipartFile.class));

    }

    @Test
    void itShouldReturnCustomerByIdWhenGetCustomerById() throws Exception {
        Customer customer = Customer.builder()
                .id("customer1")
                .nama("Sawas")
                .build();

        when(customerService.getById(anyString())).thenReturn(customer);

        mockMvc.perform(
                get("/api/v1/customer/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<Customer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getId(), "customer1");
            assertEquals(response.getData().getNama(), "Sawas");
        });
        //verify
        verify(customerService,times(1)).getById(any(String.class));
    }

    @Test
    void itShouldReturnCustomersWhenGetAllCustomer() throws Exception {
        Customer customer1 = Customer.builder()
                .id("customer1")
                .nama("Sawas")
                .build();
        Customer customer2 = Customer.builder()
                .id("customer2")
                .nama("Budi")
                .build();
        List<Customer> customerList= new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);

        when(customerService.getAll(anyString())).thenReturn(customerList);

        mockMvc.perform(
                get("/api/v1/customer/1/all-customer")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<Customer>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getNama(), "Sawas");
            assertEquals(response.getData().get(1).getNama(), "Budi");
    });
        verify(customerService,times(1)).getAll(any(String.class));
    }
}