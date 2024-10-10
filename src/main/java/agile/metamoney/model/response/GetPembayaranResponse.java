package agile.metamoney.model.response;

import agile.metamoney.entity.constant.EStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetPembayaranResponse {
    private String idPembayaran;
    private LocalDate tanggalTenggat;
    private Long hargaAwal;
    private Long totalDenda;
    private Long totalBayar;
    private EStatus statusPembayaran;
}
