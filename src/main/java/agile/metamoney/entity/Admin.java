package agile.metamoney.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "m_admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Admin {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "nama",  nullable = false)
    private String nama;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}
