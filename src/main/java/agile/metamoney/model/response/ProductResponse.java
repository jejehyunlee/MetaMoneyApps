package agile.metamoney.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {

    private String id;
    private String nama;
    private Long jumlah;
    private Integer tenor;
    private Double bunga;
    private Long denda;

}
