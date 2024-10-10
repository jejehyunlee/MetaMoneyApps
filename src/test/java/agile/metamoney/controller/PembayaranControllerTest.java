package agile.metamoney.controller;

import agile.metamoney.entity.Admin;
import agile.metamoney.entity.constant.EPayment;
import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PembayaranRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.GetPembayaranResponse;
import agile.metamoney.model.response.PembayaranResponse;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.PembayaranService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PembayaranControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PembayaranService pembayaranService;

    @Test
    void createNewPembayaran() throws Exception {
        PembayaranRequest request = PembayaranRequest.builder()
                .jumlah(1000000L)
                .metodePembayaran(EPayment.BCA)
                .idPembayaran("id_pembayaran_test")
                .idPemminjaman("id_peminjaman_test")
                .build();

        PembayaranResponse pembayaranResponse = PembayaranResponse.builder()
                .tanggal(LocalDate.now())
                .namaCustomer("foo")
                .namaProduct("PLATINUM")
                .hargaAwal(1000000L)
                .denda(0L)
                .jumlahPembayaran(1000000L)
                .metodePembayaran(EPayment.BCA)
                .message("Pembayaran Berhasil, terima kasih anda telah melunasi pinjaman anda")
                .build();

        when(pembayaranService.createNewPembayaran(any(PembayaranRequest.class))).thenReturn(pembayaranResponse);

        mockMvc.perform(
                post("/api/v1/pembayaran")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<PembayaranResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getTanggal(), LocalDate.now());
            assertEquals(response.getData().getNamaCustomer(), "foo");
            assertEquals(response.getData().getNamaProduct(), "PLATINUM");
            assertEquals(response.getData().getJumlahPembayaran(), 1000000L);
            assertEquals(response.getData().getDenda(),0);
            assertEquals(response.getData().getMessage(),"Pembayaran Berhasil, terima kasih anda telah melunasi pinjaman anda");
            assertEquals(response.getData().getMetodePembayaran(),EPayment.BCA);
        });
        //verify
        verify(pembayaranService,times(1)).createNewPembayaran(any(PembayaranRequest.class));
    }

    @Test
    void getAllPembayaran() throws Exception {
        GetPembayaranResponse getPembayaranResponse1 = GetPembayaranResponse.builder()
                .idPembayaran("abcTEST1")
                .tanggalTenggat(LocalDate.now())
                .hargaAwal(200000L)
                .totalDenda(0L)
                .totalBayar(200000L)
                .statusPembayaran(EStatus.LUNAS)
                .build();

        GetPembayaranResponse getPembayaranResponse2 = GetPembayaranResponse.builder()
                .idPembayaran("abcTEST2")
                .tanggalTenggat(LocalDate.now())
                .hargaAwal(200000L)
                .totalDenda(0L)
                .totalBayar(200000L)
                .statusPembayaran(EStatus.BELUM_LUNAS)
                .build();

        List<GetPembayaranResponse> getPembayaranResponses = new ArrayList<>();
        getPembayaranResponses.add(getPembayaranResponse1);
        getPembayaranResponses.add(getPembayaranResponse2);

        when(pembayaranService.getListPembayaran(anyString())).thenReturn(getPembayaranResponses);

        mockMvc.perform(
                get("/api/v1/pembayaran/test_id_pembayaran")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<GetPembayaranResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getIdPembayaran(), "abcTEST1");
            assertEquals(response.getData().get(0).getStatusPembayaran(), EStatus.LUNAS);
            assertEquals(response.getData().get(0).getHargaAwal(), 200000L);
            assertEquals(response.getData().get(1).getIdPembayaran(), "abcTEST2");
            assertEquals(response.getData().get(1).getStatusPembayaran(), EStatus.BELUM_LUNAS);
            assertEquals(response.getData().get(1).getHargaAwal(), 200000L);
        });
        //verify
        verify(pembayaranService,times(1)).getListPembayaran(anyString());
    }
}