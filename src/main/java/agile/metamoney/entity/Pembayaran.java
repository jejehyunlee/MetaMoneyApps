package agile.metamoney.entity;

import agile.metamoney.entity.constant.EPayment;
import agile.metamoney.entity.constant.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_pembayaran")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@EntityListeners({
        AuditingEntityListener.class
})
public class Pembayaran {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_pembayaran")
    private String id;

    @Column(name = "jumlah")
    private Long jumlah;

    @Column(name = "jatuh_tempo")
    private LocalDate jatuhTempo;

    @Column(name = "jumlah_denda")
    private Long jumlahDenda;

    @Column(name = "status_pembayaran")
    @Enumerated(EnumType.STRING)
    private EStatus statusPembayaran;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private EPayment methodPembayaran;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(name = "id_detail_peminjaman",referencedColumnName = "id_detail_peminjaman")
    private DetailPeminjaman idPeminjaman;
}
