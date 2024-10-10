package agile.metamoney.entity;

import agile.metamoney.entity.constant.EGender;
import agile.metamoney.entity.constant.ERole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.time.LocalDate;

@Entity
@Table(name = "m_customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    private EGender jenisKelamin;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "nomer_telepon",unique = true, nullable = false)
    @Size(max = 16)
    @Pattern(regexp = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)")
    private String nomerTelepon;

    @Column(name = "foto_ktp")
    private String fotoKtp;

    @Column(name = "nik", unique = true)
    @Size(min = 16, max = 16)
    private String nik;

    @Column(name = "foto_diri")
    private String fotoDiri;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nama_bank")
    private String namaBank;

    @Column(name = "no_rek", unique = true)
    private String noRek;

    @Column(name = "kota")
    private String kota;

    @Column(name = "kecamatan")
    private String kecamatan;

    @Column(name = "rw")
    private String rw;

    @Column(name = "rt")
    private String rt;

    @Column(name = "alamat_lengkap")
    private String alamatLengkap;

    @Column(name = "status_hubungan")
    private String statusHubungan;

    @Column(name = "nama_kondar")
    private String namaKondar;

    @Column(name = "notelp_kondar")
    @Pattern(regexp = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)")
    private String noTelpkondar;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_active_peminjaman", nullable = false)
    private Boolean isActivePeminjaman;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_pekerjaan",referencedColumnName = "id_pekerjaan")
    private CustomerWork customerWork;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;


}
