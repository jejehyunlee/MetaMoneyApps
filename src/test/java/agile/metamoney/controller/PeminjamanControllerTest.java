package agile.metamoney.controller;

import agile.metamoney.entity.Admin;
import agile.metamoney.entity.constant.EGender;
import agile.metamoney.entity.constant.EPayment;
import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PeminjamanRequest;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.request.UpdateStatusRequest;
import agile.metamoney.model.response.*;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.PeminjamanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PeminjamanControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PeminjamanService peminjamanService;

    @Test
    void createPeminjaman() throws Exception {
        PeminjamanRequest peminjamanRequest = PeminjamanRequest.builder()
                .tujuan("Beli Hp")
                .deskripsi("Beli Hp mengganti yang rusak")
                .idCustomer("customer1")
                .idProduct("product1")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(peminjamanRequest);

        MockMultipartFile fileFtTtd = new MockMultipartFile("ftTtd","image.jpg", MediaType.IMAGE_JPEG_VALUE,"ttd-image-content".getBytes());

        PeminjamanResponse peminjamanResponse = PeminjamanResponse.builder()
                .id("peminjaman1")
                .tujuan("Beli Hp")
                .deskripsi("Beli Hp mengganti yang rusak")
                .ttdPeminjam("image.jpg")
                .build();

        MockPart create_json= new MockPart("data-pinjaman",jsonRequest.getBytes());

        when(peminjamanService.createPinjamanBaru(any(PeminjamanRequest.class),any(MultipartFile.class))).thenReturn(peminjamanResponse);

        mockMvc.perform(multipart("/api/v1/peminjaman")
                        .file(fileFtTtd)
                        .part(create_json)
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    CommonResponse<PeminjamanResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(response.getData().getId(), "peminjaman1");
                    assertEquals(response.getData().getTujuan(), "Beli Hp");
                    assertEquals(response.getData().getDeskripsi(), "Beli Hp mengganti yang rusak");
                    assertEquals(response.getData().getTtdPeminjam(), "image.jpg");


                });
        verify(peminjamanService,times(1)).createPinjamanBaru(any(PeminjamanRequest.class),any(MultipartFile.class));
    }

    @Test
    void updateStatusPeminjaman() throws Exception {
        UpdateStatusRequest updateStatusRequest = UpdateStatusRequest.builder()
                .idAdmin("test_id_admin")
                .idPeminjaman("test_id_peminjaman")
                .status(EStatus.DITERIMA)
                .build();

        PeminjamanResponse peminjamanResponse = PeminjamanResponse.builder()
                .id("peminjaman_id")
                .tanggalPeminjaman(LocalDate.now())
                .tujuan("test tujuan")
                .deskripsi("test dekripsi")
                .status(EStatus.DITERIMA)
                .product(ProductResponse.builder()
                        .id("test_idProduct")
                        .nama("test_namaProduct")
                        .jumlah(20000000L)
                        .tenor(12)
                        .bunga(0.1)
                        .denda(0L)
                        .build())
                .customerResponse(CustomerResponse.builder()
                        .tanggalLahir(LocalDate.now())
                        .jenisKelamin(EGender.PRIA)
                        .nik("3123456789010003")
                        .namaBank("BRI")
                        .noRek("123456789")
                        .kota("jakarta")
                        .kecamatan("pancoran")
                        .rw("001")
                        .rt("001")
                        .alamatLengkap("test_alamat")
                        .statusHubungan("test_status")
                        .namaKondar("test_nama_kontak darurat")
                        .noTelpkondar("021312132131")
                        .isActive(true)
                        .workResponse(CustomerWorkResponse.builder()
                                .tipePekerjaan("karyawan")
                                .bidangPekerjaan("kesehatan")
                                .namaPekerjaan("teller")
                                .namaPerusahaan("pt angin badai")
                                .nomerTelepon("081123123213")
                                .build())
                        .build())
                .ttdPeminjam("gambar.png")
                .build();

        when(peminjamanService.updateStatusPeminjaman(any(UpdateStatusRequest.class))).thenReturn(peminjamanResponse);

        mockMvc.perform(
                put("/api/v1/peminjaman")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStatusRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<PeminjamanResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getId(), "peminjaman_id");
            assertEquals(response.getData().getTujuan(), "test tujuan");
            assertEquals(response.getData().getDeskripsi(), "test dekripsi");
        });
        //verify
        verify(peminjamanService,times(1)).updateStatusPeminjaman(any(UpdateStatusRequest.class));
    }
}