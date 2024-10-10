package agile.metamoney.model.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterCustomerResponse {
    private String id;
    private String username;
    private String nama;
    private String email;
    private String nomerTelepon;
}
