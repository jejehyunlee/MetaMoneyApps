package agile.metamoney.entity;

import agile.metamoney.entity.constant.EStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_peminjaman")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@EntityListeners({
        AuditingEntityListener.class
})
public class Peminjaman {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_peminjaman")
    private String id;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "tujuan_peminjaman")
    private String tujuanPeminjaman;

    @Column(name = "deskripsi_peminjaman")
    private String deskripsiPeminjaman;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "jumlah_pinjaman")
    private Long jumlahPinjaman;

    @ManyToOne
    @JoinColumn(name = "id_customer",referencedColumnName = "id")
    private Customer idCustomer;

//    @OneToOne
//    @JoinColumn(name = "id_detail_peminjaman", referencedColumnName = "id")
//    private DetailPeminjaman idDetailPeminjaman;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate updatedAt;

    @Column(name = "ttd_peminjam")
    private String ttdPeminjam;

}
