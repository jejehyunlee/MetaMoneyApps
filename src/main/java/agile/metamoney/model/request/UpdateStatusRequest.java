package agile.metamoney.model.request;

import agile.metamoney.entity.constant.EStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateStatusRequest {
    @NotBlank(message = "id admin must not blank")
    private String idAdmin;
    private String idPeminjaman;
    private EStatus status;
}
