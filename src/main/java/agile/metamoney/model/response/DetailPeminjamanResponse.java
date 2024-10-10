package agile.metamoney.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DetailPeminjamanResponse {
    private Long sisaPinjam;
    private ProductResponse productResponse;
    private PeminjamanResponse peminjamanResponse;
}
