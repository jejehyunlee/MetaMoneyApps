package agile.metamoney.service.impl;

import agile.metamoney.entity.*;
import agile.metamoney.entity.constant.EPayment;
import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PembayaranRequest;
import agile.metamoney.model.response.GetPembayaranResponse;
import agile.metamoney.model.response.PembayaranResponse;
import agile.metamoney.repository.DetailPeminjamanRepository;
import agile.metamoney.repository.PembayaranRepository;
import agile.metamoney.repository.PeminjamanRepository;
import agile.metamoney.repository.ProductRepository;
import agile.metamoney.service.PembayaranService;
import agile.metamoney.service.PeminjamanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PembayaranServiceImpl implements PembayaranService {
    private final PembayaranRepository pembayaranRepository;
    private final PeminjamanRepository peminjamanRepository;
    private final ProductRepository productRepository;
    private final DetailPeminjamanRepository detailPeminjamanRepository;

    @Transactional
    @Override
    public PembayaranResponse createNewPembayaran(PembayaranRequest request) {
        Peminjaman currentPeminjaman = peminjamanRepository.findById(request.getIdPemminjaman()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id peminjaman no found, failed create pembayaran")
        );

        DetailPeminjaman detailPeminjaman = detailPeminjamanRepository.findFirstByIdPeminjaman(currentPeminjaman).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id detail peminjaman not found, failed create pembayaran")
        );

        Pembayaran currentPembayaran = pembayaranRepository.findById(request.getIdPembayaran()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id pembayaran not found, failed create pembayaran")
        );

        if (currentPembayaran.getStatusPembayaran() == EStatus.LUNAS || currentPeminjaman.getStatus() == EStatus.LUNAS){
            return PembayaranResponse.builder()
                    .tanggal(LocalDate.now())
                    .namaCustomer(detailPeminjaman.getIdPeminjaman().getIdCustomer().getNama())
                    .namaProduct(detailPeminjaman.getIdProduct().getNama())
                    .hargaAwal(0L)
                    .denda(0L)
                    .metodePembayaran(null)
                    .jumlahPembayaran(0L)
                    .message("Pembayaran ini Sudah Lunas")
                    .build();
        } else if (currentPeminjaman.getStatus() == EStatus.PENDING) {
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "status pibnjaman is PENDING, failed create pembayaran");
        }

        Product currentProduct = productRepository.findById(detailPeminjaman.getIdProduct().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id product not found, failed create pembayaran")
        );

        // Hitung Pembayaran + Bunga 0.1 / Perbulan 30 hari sesudah pencairan
        Double hargaA = currentProduct.getJumlah() + (currentProduct.getJumlah() * currentProduct.getBunga());
        Long hargaAwal = Math.round(hargaA / currentProduct.getTenor());

        // Hitung Denda sehari 1rb
        Long currentProductDenda = currentProduct.getDenda();
        LocalDate jatuhTempo = currentPembayaran.getJatuhTempo();
        LocalDate waktuSekarang = LocalDate.now();
        Long hariSekarang = waktuSekarang.toEpochDay();
        Long hariJatuhTempo = jatuhTempo.toEpochDay();
        long sisaHari = hariSekarang - hariJatuhTempo;

        Long totalDenda = 0L;
        if (sisaHari > 30){
            totalDenda = (sisaHari - 30) * currentProductDenda;
        }

        Long totalBayar = (totalDenda + hargaAwal);

        if (request.getJumlah() < totalBayar){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kamu membayar kurang dari jumlah " + totalBayar);
        } else if (request.getJumlah() >= totalBayar+1100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kamu membayar lebih dari jumlah " + totalBayar);
        }

        String message = null;
        // Hitung Tenor Jika Pertama kali bayar ambil tenor dari product lalu dikurangi 1
        //Objects.equals(detailPeminjaman.getSisaPinjaman(), hargaA)
        if (detailPeminjaman.getSisaTenor() > 0){
            detailPeminjaman.setSisaTenor(detailPeminjaman.getSisaTenor() - 1 );
            message = "Pembayaran Berhasil, Sisa Cicilan anda " + detailPeminjaman.getSisaTenor() + " bulan lagi.";
            detailPeminjaman.getIdPeminjaman().setStatus(EStatus.BELUM_LUNAS);
        }
        if (detailPeminjaman.getSisaTenor() == 0) {
            //set peminjaman
            detailPeminjaman.getIdPeminjaman().setStatus(EStatus.LUNAS);
            //set customer
            detailPeminjaman.getIdPeminjaman().getIdCustomer().setIsActivePeminjaman(Boolean.FALSE);
            message = "Pembayaran Berhasil, terima kasih anda telah melunasi pinjaman anda";

        }
        //set pembayaran
        currentPembayaran.setMethodPembayaran(request.getMetodePembayaran());
        currentPembayaran.setJumlahDenda(totalDenda);
        currentPembayaran.setStatusPembayaran(EStatus.LUNAS);

        // Hitung Total Jumlah Peminjaman dikurangi Jumlah Pembayaran perbulan
        Long sisaPinjam = detailPeminjaman.getSisaPinjaman();
        if (sisaPinjam <= hargaAwal){
            detailPeminjaman.setSisaPinjaman(0L);
        }else {
            detailPeminjaman.setSisaPinjaman(detailPeminjaman.getSisaPinjaman() - hargaAwal);
        }

        return PembayaranResponse.builder()
                .tanggal(LocalDate.now())
                .namaCustomer(detailPeminjaman.getIdPeminjaman().getIdCustomer().getNama())
                .namaProduct(detailPeminjaman.getIdProduct().getNama())
                .metodePembayaran(request.getMetodePembayaran())
                .hargaAwal(hargaAwal)
                .denda(totalDenda)
                .jumlahPembayaran(request.getJumlah())
                .message(message)
                .build();
    }

//    @Transactional
//    @Override
//    public GetPembayaranResponse getPembayaranByIdPeminjaman(PembayaranRequest request, String idPeminjaman) {
//        Peminjaman currentPeminjaman = peminjamanRepository.findFirstByIdAndStatus(idPeminjaman,EStatus.DITERIMA).orElse(null);
//        if(currentPeminjaman == null){
//            return  GetPembayaranResponse.builder()
//                    .hargaAwal(0L)
//                    .totalDenda(0L)
//                    .totalBayar(0L)
//                    .tanggalTenggat(null)
//                    .build();
//        }
//        DetailPeminjaman detailPeminjaman = detailPeminjamanRepository.findFirstByIdPeminjaman(currentPeminjaman).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id peminjaman not found, failed create pembayaran")
//        );
//
//        Product currentProduct = productRepository.findById(detailPeminjaman.getIdProduct().getId()).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id product not found, failed create pembayaran")
//        );
//
//        // Hitung Pembayaran + Bunga 0.1 / Perbulan 30 hari sesudah pencairan
//        Double hargaA = currentProduct.getJumlah() + (currentProduct.getJumlah() * currentProduct.getBunga());
//        Double hargaAwal = hargaA / currentProduct.getTenor();
//
//        // Hitung Denda sebulan 30rb
//        Long currentProductDenda = currentProduct.getDenda();
//        LocalDate updatedAt = currentPeminjaman.getUpdatedAt().minusDays(40);
//        LocalDate waktuSekarang = LocalDate.now();
//        LocalDate tenggat = currentPeminjaman.getUpdatedAt().plusDays(30);
//
//        Long hariSekarang = waktuSekarang.toEpochDay();
//        Long hariUpdate = updatedAt.toEpochDay();
//        Long sisaHari = hariSekarang - hariUpdate;
//
//        Long totalDenda = 0L;
//        if (sisaHari > 30){
//            totalDenda = (long) ((sisaHari - 30) * currentProductDenda);
//        }
//
//        Double totalBayar = totalDenda + hargaAwal;
//
//        return GetPembayaranResponse.builder()
//                .hargaAwal(Math.round(hargaAwal))
//                .totalDenda(totalDenda)
//                .totalBayar(Math.round(totalBayar))
//                .tanggalTenggat(tenggat)
//                .build();
//    }

    @Override
    public void setPembayaran(String idProduct, DetailPeminjaman idPeminjman) {
        Product currentProduct = productRepository.findById(idProduct).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id product not found, failed create pembayaran")
        );

        Integer tenor = currentProduct.getTenor();
        Double hargaA = currentProduct.getJumlah() + (currentProduct.getJumlah() * currentProduct.getBunga());
        Long hargaAwal = Math.round(hargaA / currentProduct.getTenor());

        for (int i = 0; i < tenor; i++) {
            int month=30*(i+1);
            Pembayaran pembayaran = Pembayaran.builder()
                    .methodPembayaran(null)
                    .idPeminjaman(idPeminjman)
                    .jatuhTempo(LocalDate.now().plusDays(month))
                    .jumlah(hargaAwal)
                    .jumlahDenda(0L)
                    .statusPembayaran(EStatus.BELUM_LUNAS)
                    .build();
            pembayaranRepository.save(pembayaran);
        }

    }

    @Override
    public List<GetPembayaranResponse> getListPembayaran(String idPeminjaman) {
        Peminjaman currentPeminjaman = peminjamanRepository.findById(idPeminjaman).orElse(null);

        DetailPeminjaman detailPeminjaman = detailPeminjamanRepository.findFirstByIdPeminjaman(currentPeminjaman).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id peminjaman not found, failed create pembayaran")
        );
        List<GetPembayaranResponse> pembayaranResponses = new ArrayList<>();
        List<Pembayaran> pembayaranByIdPeminjaman = pembayaranRepository.findPembayaranByIdPeminjaman(detailPeminjaman);
        pembayaranByIdPeminjaman.forEach(pembayaran -> {
            pembayaranResponses.add(GetPembayaranResponse.builder()
                    .idPembayaran(pembayaran.getId())
                    .tanggalTenggat(pembayaran.getJatuhTempo())
                    .hargaAwal(pembayaran.getJumlah())
                    .totalDenda(pembayaran.getJumlahDenda())
                    .totalBayar(pembayaran.getJumlah())
                    .statusPembayaran(pembayaran.getStatusPembayaran())
                    .build());
        });

        return pembayaranResponses;
    }


//    @Override
//    public ResponseEntity<PembayaranResponse> createNewPembayaran(PembayaranRequest request, String idPeminjaman) {
//        String apiUrl = "https://api.xendit.co/payment_requests";
//        //mengatur header request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth("xnd_development_QHboCyoxQJJDE1Z7vUQoiqeaI9I01CBq7x1vDtiPaWo9Fq91smmFwO613JXD", "");
//
//        //membungkus data request dalam HttpEntity
//        HttpEntity<PembayaranRequest> requestentity = new HttpEntity<>(request, headers);
//
//        //response Posts
//        ResponseEntity<PembayaranResponse> responseEntity = restTemplate.postForEntity(apiUrl,requestentity,PembayaranResponse.class);
//        if (responseEntity.getStatusCode().is2xxSuccessful()){
//            PembayaranResponse responseBody = responseEntity.getBody();
//            Pembayaran pembayaran = new Pembayaran();
//            pembayaran.setJumlah(responseBody.getAmount());
//            log.info("TEST : "+ responseBody.getAmount());
////            pembayaranRepository.save(pembayaran);
//
//
//            return ResponseEntity.ok(PembayaranResponse.builder()
//                            .amount(responseBody.getAmount())
//                            .build());
//        }else {
//            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
//        }
//    }
}
