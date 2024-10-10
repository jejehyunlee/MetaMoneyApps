package agile.metamoney.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Entity
@Table(name = "t_detail_peminjaman")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class DetailPeminjaman {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_detail_peminjaman")
    private String id;

    @Column(name = "sisa_pinjaman", columnDefinition = "double check (sisa_pinjaman >= 0)")
    private Long sisaPinjaman;

    @Column(name = "sisa_tenor", columnDefinition = "double check (sisa_tenor >= 0)")
    private Integer sisaTenor;

    @OneToOne
    @JoinColumn(name = "id_product",referencedColumnName = "id")
    private Product idProduct;

    @OneToOne
    @JoinColumn(name = "id_peminjaman",referencedColumnName = "id_peminjaman")
    private Peminjaman idPeminjaman;
}
