package agile.metamoney.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImageResponse {
    private Boolean isActive;
    private String fotoKtp;

    private String fotoDiri;
}
