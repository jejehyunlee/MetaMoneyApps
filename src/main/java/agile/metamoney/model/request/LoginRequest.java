package agile.metamoney.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoginRequest {
    @NotBlank
    @NonNull
    @Size(max = 255, message = "username size not more than 255")
    private String username;

    @NotBlank
    @NonNull
    @Size(max = 255, message = "password size not more than 255")
    private String password;

}

