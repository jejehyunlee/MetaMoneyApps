package agile.metamoney.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateCustomerWorkRequest {
    private String tipePekerjaan;

    private String bidangPekerjaan;

    private String namaPekerjaan;

    private String namaPerusahaan;

    private String nomerTelepon;
}
