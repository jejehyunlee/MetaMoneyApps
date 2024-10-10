package agile.metamoney.model.response;

import agile.metamoney.entity.constant.EGender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerResponse {
    private String id;
    private String name;
    private EGender jenisKelamin;

    private LocalDate tanggalLahir;

    private String fotoKtp;

    private String fotoDiri;

    private String nik;

    private String namaBank;

    private String noRek;

    private String kota;
    private String email;
    private String nomerTelepon;

    private String kecamatan;

    private String rw;

    private String rt;

    private String alamatLengkap;

    private String statusHubungan;

    private String namaKondar;

    private String noTelpkondar;

    private Boolean isActive;
    private CustomerWorkResponse workResponse;
}
