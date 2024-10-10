package agile.metamoney.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerWorkResponse {
    private String tipePekerjaan;

    private String bidangPekerjaan;

    private String namaPekerjaan;

    private String namaPerusahaan;

    private String nomerTelepon;
}

