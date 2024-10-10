package agile.metamoney.model.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PeminjamanRequest {

    private String tujuan;
    private String deskripsi;
    private String idProduct;
    private String idCustomer;
}
