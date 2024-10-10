package agile.metamoney.model.response;

import agile.metamoney.entity.constant.EStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PeminjamanResponse {
    private String id;
    private LocalDate tanggalPeminjaman;
    private String tujuan;
    private String deskripsi;
    private EStatus status;
    private ProductResponse product;
    private CustomerResponse customerResponse;
    private String ttdPeminjam;
}
