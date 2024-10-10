package agile.metamoney.service.impl;

import agile.metamoney.entity.*;
import agile.metamoney.entity.constant.EPayment;
import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PembayaranRequest;
import agile.metamoney.model.response.GetPembayaranResponse;
import agile.metamoney.model.response.PembayaranResponse;
import agile.metamoney.repository.*;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.PembayaranService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
class PembayaranServiceImplTest {
    private final PembayaranRepository pembayaranRepository = mock(PembayaranRepository.class);
    private final PeminjamanRepository peminjamanRepository = mock(PeminjamanRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final DetailPeminjamanRepository detailPeminjamanRepository = mock(DetailPeminjamanRepository.class);
    private final PembayaranService pembayaranService = new PembayaranServiceImpl(pembayaranRepository,peminjamanRepository, productRepository, detailPeminjamanRepository);

    @Test
    void testCreatePembayaranSuccess() {
        Customer customer = Customer.builder()
                .id("test")
                .isActivePeminjaman(true)
                .build();

        Peminjaman peminjaman = Peminjaman.builder()
                .id("test")
                .status(EStatus.BELUM_LUNAS)
                .idCustomer(customer)
                .build();
        peminjaman.setStatus(EStatus.BELUM_LUNAS);

        Product product = Product.builder()
                .id("test")
                .nama("RUBY")
                .jumlah(30000000L)
                .tenor(3)
                .bunga(0.1)
                .denda(1000L)
                .build();

        DetailPeminjaman detailPeminjaman = DetailPeminjaman.builder()
                .sisaPinjaman(33000000L)
                .sisaTenor(3)
                .idProduct(product)
                .idPeminjaman(peminjaman)
                .build();

        Pembayaran pembayaran = Pembayaran.builder()
                .jatuhTempo(LocalDate.now())
                .build();

        pembayaran.setStatusPembayaran(EStatus.BELUM_LUNAS);

        PembayaranRequest request = PembayaranRequest.builder()
                .jumlah(11000000L)
                .metodePembayaran(EPayment.BCA)
                .idPembayaran("id_pembayaran_test")
                .idPemminjaman("id_peminjaman_test")
                .build();

        PembayaranResponse pembayaranResponse = PembayaranResponse.builder()
                .tanggal(LocalDate.now())
                .namaCustomer("foo")
                .namaProduct("RUBY")
                .hargaAwal(11000000L)
                .denda(0L)
                .jumlahPembayaran(11000000L)
                .metodePembayaran(EPayment.BCA)
                .message("Pembayaran Berhasil, Sisa Cicilan anda 2 bulan lagi.")
                .build();


        when(peminjamanRepository.findById(anyString())).thenReturn(Optional.ofNullable(peminjaman));
        when(detailPeminjamanRepository.findFirstByIdPeminjaman(any(Peminjaman.class))).thenReturn(Optional.ofNullable(detailPeminjaman));
        when(pembayaranRepository.findById(anyString())).thenReturn(Optional.ofNullable(pembayaran));
        when(productRepository.findById("test")).thenReturn(Optional.ofNullable(product));

        PembayaranResponse createPembayaran = pembayaranService.createNewPembayaran(request);
        assertEquals(createPembayaran.getTanggal(), LocalDate.now());
        assertEquals(createPembayaran.getMetodePembayaran(), pembayaranResponse.getMetodePembayaran());
        assertEquals(createPembayaran.getMessage(), pembayaranResponse.getMessage());

    }

    @Test
    void testCreatePembayaranFailed() {
        Customer customer = Customer.builder()
                .id("test")
                .isActivePeminjaman(true)
                .build();

        Peminjaman peminjaman = Peminjaman.builder()
                .id("test")
                .status(EStatus.LUNAS)
                .idCustomer(customer)
                .build();
        peminjaman.setStatus(EStatus.BELUM_LUNAS);

        Product product = Product.builder()
                .id("test")
                .nama("RUBY")
                .jumlah(30000000L)
                .tenor(3)
                .bunga(0.1)
                .denda(1000L)
                .build();

        DetailPeminjaman detailPeminjaman = DetailPeminjaman.builder()
                .sisaPinjaman(33000000L)
                .sisaTenor(3)
                .idProduct(product)
                .idPeminjaman(peminjaman)
                .build();

        Pembayaran pembayaran = Pembayaran.builder()
                .jatuhTempo(LocalDate.now())
                .build();

        pembayaran.setStatusPembayaran(EStatus.LUNAS);

        PembayaranRequest request = PembayaranRequest.builder()
                .jumlah(11000000L)
                .metodePembayaran(EPayment.BCA)
                .idPembayaran("id_pembayaran_test")
                .idPemminjaman("id_peminjaman_test")
                .build();

        when(peminjamanRepository.findById(anyString())).thenReturn(Optional.ofNullable(peminjaman));
        when(detailPeminjamanRepository.findFirstByIdPeminjaman(any(Peminjaman.class))).thenReturn(Optional.ofNullable(detailPeminjaman));
        when(pembayaranRepository.findById(anyString())).thenReturn(Optional.ofNullable(pembayaran));
        when(productRepository.findById("test")).thenReturn(Optional.ofNullable(product));

        PembayaranResponse createPembayaran = pembayaranService.createNewPembayaran(request);
        assertEquals(createPembayaran.getTanggal(), LocalDate.now());
        assertEquals(createPembayaran.getMessage(),"Pembayaran ini Sudah Lunas");

    }

    @Test
    void getPembayaranByIdPeminjaman() {

    }

    @Test
    void setPembayaran() {
        Product product = Product.builder()
                .id("test")
                .nama("RUBY")
                .jumlah(30000000L)
                .tenor(3)
                .bunga(0.1)
                .denda(1000L)
                .build();
        Pembayaran pembayaran = Pembayaran.builder()
                .id("test")
                .build();
        Peminjaman peminjaman = Peminjaman.builder()
                .id("test")
                .status(EStatus.BELUM_LUNAS)
                .build();
        peminjaman.setStatus(EStatus.BELUM_LUNAS);
        DetailPeminjaman detailPeminjaman = DetailPeminjaman.builder()
                .sisaPinjaman(33000000L)
                .sisaTenor(3)
                .idProduct(product)
                .idPeminjaman(peminjaman)
                .build();
        when(productRepository.findById("test")).thenReturn(Optional.ofNullable(product));
        when(pembayaranRepository.save(any(Pembayaran.class))).thenReturn(pembayaran);
        pembayaranService.setPembayaran("test", detailPeminjaman);
        verify(pembayaranRepository, times(3)).save(any(Pembayaran.class));

    }

    @Test
    void getListPembayaran() {
        Peminjaman peminjaman = Peminjaman.builder()
                .id("test")
                .status(EStatus.LUNAS)
                .build();

        Product product = Product.builder()
                .id("test")
                .nama("RUBY")
                .jumlah(30000000L)
                .tenor(3)
                .bunga(0.1)
                .denda(1000L)
                .build();

        DetailPeminjaman detailPeminjaman = DetailPeminjaman.builder()
                .sisaPinjaman(33000000L)
                .sisaTenor(3)
                .idProduct(product)
                .idPeminjaman(peminjaman)
                .build();

        Pembayaran pembayaran = Pembayaran.builder()
                .id("test")
                .jatuhTempo(LocalDate.now())
                .methodPembayaran(EPayment.BCA)
                .statusPembayaran(EStatus.BELUM_LUNAS)
                .jumlah(11000000L)
                .build();

        Pembayaran pembayaran2 = Pembayaran.builder()
                .id("test")
                .jatuhTempo(LocalDate.now())
                .methodPembayaran(EPayment.BCA)
                .statusPembayaran(EStatus.BELUM_LUNAS)
                .jumlah(11000000L)
                .build();
        List<Pembayaran> pembayarans = new ArrayList<>();
        pembayarans.add(pembayaran);
        pembayarans.add(pembayaran2);

        when(detailPeminjamanRepository.findFirstByIdPeminjaman(any(Peminjaman.class))).thenReturn(Optional.ofNullable(detailPeminjaman));
        when(peminjamanRepository.findById(anyString())).thenReturn(Optional.ofNullable(peminjaman));
        when(pembayaranRepository.findPembayaranByIdPeminjaman(any(DetailPeminjaman.class))).thenReturn(pembayarans);

        List<GetPembayaranResponse> listPembayaran = pembayaranService.getListPembayaran("test");
        assertNotNull(listPembayaran);
        assertEquals(listPembayaran.size(), 2);
        assertEquals(listPembayaran.get(0).getIdPembayaran(), "test");

    }
}