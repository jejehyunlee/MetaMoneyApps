package agile.metamoney.model.request;

import agile.metamoney.entity.constant.EPayment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PembayaranRequest {
    private Long jumlah;
    private EPayment metodePembayaran;
    private String idPemminjaman;
    private String idPembayaran;

}
