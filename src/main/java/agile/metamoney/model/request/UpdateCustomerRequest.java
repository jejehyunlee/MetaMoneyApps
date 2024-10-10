package agile.metamoney.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateCustomerRequest {
    @NotBlank(message = "id must not blank")
    @NonNull
    private String id;

    @NotBlank(message = "jenis kelamin must not blank")
    @NonNull
    private String jenisKelamin;

    @NonNull
    private String tanggalLahir;

    @NotBlank(message = "no telp must not blank")
    private String nomerTelepon;

    @NotBlank(message = "email must not blank")
    @Email(message = "Email not valid")
    private String email;
//    @NonNull
//    private Blob fotoKtp;

//    @NonNull
//    private Blob fotoDiri;

    @NotBlank(message = "nik must not blank")
    @NonNull
    private String nik;

    @NotBlank(message = "nama bank must not blank")
    @NonNull
    private String namaBank;

    @NotBlank(message = "no rek must not blank")
    @NonNull
    private String noRek;

    @NotBlank(message = "kota must not blank")
    @NonNull
    private String kota;

    @NotBlank(message = "kecamatan must not blank")
    @NonNull
    private String kecamatan;

    @NotBlank(message = "rw must not blank")
    @NonNull
    private String rw;

    @NotBlank(message = "rt must not blank")
    @NonNull
    private String rt;

    @NotBlank(message = "alamat lengkap must not blank")
    @NonNull
    private String alamatLengkap;

    @NotBlank(message = "status hubungan must not blank")
    @NonNull
    private String statusHubungan;

    @NotBlank(message = "nama kondar must not blank")
    @NonNull
    private String namaKondar;

    @NotBlank(message = "no telepon kondar must not blank")
    @NonNull
    private String noTelpkondar;

    @NonNull
    @JsonIgnore
    private Boolean isActive;

    private UpdateCustomerWorkRequest customerWork;
}
