package agile.metamoney.model.response;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.entity.constant.EPayment;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PembayaranResponse {
    private LocalDate tanggal;
    private String namaCustomer;
    private String namaProduct;
    private Long hargaAwal;
    private Long denda;
    private Long jumlahPembayaran;
    private EPayment metodePembayaran;
    private String message;
//    private DetailPeminjamanResponse detailPeminjamanResponse;
}
