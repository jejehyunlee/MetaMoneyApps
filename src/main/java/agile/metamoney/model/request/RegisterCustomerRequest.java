package agile.metamoney.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterCustomerRequest {

    @NotBlank
    @NonNull
    @Size(max = 255, message = "name size not more than 255")
    private String name;

    @NotBlank
    @NonNull
    @Size(max = 255, message = "email size not more than 255")
    private String email;

    @NotBlank
    @NonNull
    @Size(max = 255, message = "nomer telepon size not more than 255")
    private String nomerTelepon;

    @NotBlank
    @NonNull
    @Size(max = 255, message = "username size not more than 255")
    private String username;

    @NotBlank
    @NonNull
    @Size(max = 255, message = "password size not more than 255")
    private String password;

}
