package agile.metamoney.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterAdminResponse {
    private String id;
    private String nama;
    private String username;
}
