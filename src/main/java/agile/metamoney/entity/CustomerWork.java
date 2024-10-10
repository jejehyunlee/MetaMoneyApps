package agile.metamoney.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "m_cust_pekerjaan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class CustomerWork {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_pekerjaan")
    private String id;

    @Column(name = "tipe_pekerjaan")
    private String tipePekerjaan;

    @Column(name = "bidang_pekerjaan")
    private String bidangPekerjaan;

    @Column(name = "nama_pekerjaan")
    private String namaPekerjaan;

    @Column(name = "nama_perusahaan")
    private String namaPerusahaan;

    @Column(name = "nomer_telepon")
    @Pattern(regexp = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)")
    private String nomerTelepon;
}
