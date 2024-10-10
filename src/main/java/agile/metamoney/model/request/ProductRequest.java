package agile.metamoney.model.request;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {

    @NotBlank(message = "id admin must not blank")
    private String idAdmin;

    @NotBlank(message = "nama must not blank")
    private String nama;

    @NotNull(message = "jumlah must not blank")
    private Long jumlah;

    @NotNull(message = "tenor must not blank")
    private Integer tenor;

    @NotNull(message = "bunga must not blank")
    private Double bunga;

    @NotNull(message = "denda must not blank")
    private Long denda;
}
