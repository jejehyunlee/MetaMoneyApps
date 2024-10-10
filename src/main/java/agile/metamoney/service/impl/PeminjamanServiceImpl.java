package agile.metamoney.service.impl;

import agile.metamoney.entity.*;
import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PeminjamanRequest;
import agile.metamoney.model.request.UpdateStatusRequest;
import agile.metamoney.model.response.CustomerResponse;
import agile.metamoney.model.response.CustomerWorkResponse;
import agile.metamoney.model.response.PeminjamanResponse;
import agile.metamoney.model.response.ProductResponse;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.repository.CustomerRepository;
import agile.metamoney.repository.DetailPeminjamanRepository;
import agile.metamoney.repository.PeminjamanRepository;
import agile.metamoney.service.DetailPeminjamanService;
import agile.metamoney.service.PembayaranService;
import agile.metamoney.service.PeminjamanService;
import agile.metamoney.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeminjamanServiceImpl implements PeminjamanService{
    private final PeminjamanRepository peminjamanRepository;
    private final DetailPeminjamanService detailPeminjamanService;
    private final ValidationServiceImpl validationService;
    private final CustomerRepository customerRepository;
    private final ProductService productService;
    private final DetailPeminjamanRepository detailPeminjamanRepository;
    private final AdminRepository adminRepository;
    private final PembayaranService pembayaranService;

    private static PeminjamanResponse toPeminjamanResponse(Customer currentCustomer, Product currentProduct, Peminjaman peminjaman) {
        return PeminjamanResponse.builder()
                .id(peminjaman.getId())
                .tanggalPeminjaman(peminjaman.getTanggal())
                .tujuan(peminjaman.getTujuanPeminjaman())
                .deskripsi(peminjaman.getDeskripsiPeminjaman())
                .status(peminjaman.getStatus())
                .product(ProductResponse.builder()
                        .id(currentProduct.getId())
                        .nama(currentProduct.getNama())
                        .jumlah(currentProduct.getJumlah())
                        .tenor(currentProduct.getTenor())
                        .bunga(currentProduct.getBunga())
                        .denda(currentProduct.getDenda())
                        .build())
                .customerResponse(CustomerResponse.builder()
                        .id(currentCustomer.getId())
                        .name(currentCustomer.getNama())
                        .fotoKtp(currentCustomer.getFotoKtp())
                        .fotoDiri(currentCustomer.getFotoDiri())
                        .email(currentCustomer.getEmail())
                        .nomerTelepon(currentCustomer.getNomerTelepon())
                        .tanggalLahir(currentCustomer.getTanggalLahir())
                        .jenisKelamin(currentCustomer.getJenisKelamin())
                        .nik(currentCustomer.getNik())
                        .namaBank(currentCustomer.getNamaBank())
                        .noRek(currentCustomer.getNoRek())
                        .kota(currentCustomer.getKota())
                        .kecamatan(currentCustomer.getKecamatan())
                        .rw(currentCustomer.getRw())
                        .rt(currentCustomer.getRt())
                        .alamatLengkap(currentCustomer.getAlamatLengkap())
                        .statusHubungan(currentCustomer.getStatusHubungan())
                        .namaKondar(currentCustomer.getNamaKondar())
                        .noTelpkondar(currentCustomer.getNoTelpkondar())
                        .isActive(currentCustomer.getIsActive())
                        .workResponse(CustomerWorkResponse.builder()
                                .tipePekerjaan(currentCustomer.getCustomerWork().getTipePekerjaan())
                                .bidangPekerjaan(currentCustomer.getCustomerWork().getBidangPekerjaan())
                                .namaPekerjaan(currentCustomer.getCustomerWork().getNamaPekerjaan())
                                .namaPerusahaan(currentCustomer.getCustomerWork().getNamaPerusahaan())
                                .nomerTelepon(currentCustomer.getCustomerWork().getNomerTelepon())
                                .build())
                        .build())
                .ttdPeminjam(peminjaman.getTtdPeminjam())
                .build();
    }

    @Value("${file.upload-dir}")
    private String filePath;
    @Transactional
    @Override
    public PeminjamanResponse createPinjamanBaru(PeminjamanRequest request, MultipartFile ftTtd) throws IOException {
        validationService.validate(request);
        String randomNameFile= UUID.randomUUID().toString();
        String fileFtTtd= randomNameFile+ftTtd.getOriginalFilename();
        Customer currentCustomer = customerRepository.findFirstByIdAndIsActive(request.getIdCustomer(), true).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found failed create pinjaman")
        );
        if (currentCustomer.getIsActivePeminjaman()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "create pinjaman failed");
        }
        currentCustomer.setIsActivePeminjaman(true);

        Product currentProduct = productService.getById(request.getIdProduct());

        Peminjaman peminjaman = Peminjaman.builder()
                .idCustomer(currentCustomer)
                .tanggal(LocalDate.now())
                .tujuanPeminjaman(request.getTujuan())
                .deskripsiPeminjaman(request.getDeskripsi())
                .status(EStatus.PENDING)
                .jumlahPinjaman(currentProduct.getJumlah())
                .ttdPeminjam(fileFtTtd)
                .build();
        peminjamanRepository.save(peminjaman);

        Path pathFtTtd = Path.of(filePath+fileFtTtd);
        ftTtd.transferTo(pathFtTtd);
        detailPeminjamanService.createDetailPeminjaman(request, peminjaman);

        log.info("Customer mengajukan pinjaman status DITUNDA dengan pinjaman id : " + peminjaman.getId());
        return toPeminjamanResponse(currentCustomer, currentProduct, peminjaman);
    }


    @Transactional
    @Override
    public PeminjamanResponse updateStatusPeminjaman(UpdateStatusRequest request) {
        validationService.validate(request);
        adminRepository.findById(request.getIdAdmin()).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Create Product Failed"));

        Peminjaman currentPeminjaman = peminjamanRepository.findById(request.getIdPeminjaman()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id peminjaman not found, failed update status")
        );
        DetailPeminjaman detailPeminjaman = detailPeminjamanRepository.findFirstByIdPeminjaman(currentPeminjaman).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id peminjaman not found, failed update status")
        );

        pembayaranService.setPembayaran(detailPeminjaman.getIdProduct().getId(),detailPeminjaman);
        currentPeminjaman.setStatus(request.getStatus());
        peminjamanRepository.save(currentPeminjaman);

        log.info("admin set status pinjaman DITERIMA : " + request.getIdPeminjaman());
        return toPeminjamanResponse(currentPeminjaman.getIdCustomer(),detailPeminjaman.getIdProduct(), currentPeminjaman);
    }
}
