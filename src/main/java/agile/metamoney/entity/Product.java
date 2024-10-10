package agile.metamoney.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "m_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Product {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "nama",unique = true, nullable = false)
    private String nama;

    @Column(name = "jumlah", nullable = false)
    private Long jumlah;

    @Column(name = "tenor", nullable = false)
    private Integer tenor;

    @Column(name = "bunga", nullable = false)
    private Double bunga;

    @Column(name = "denda", nullable = false)
    private Long denda;


}
