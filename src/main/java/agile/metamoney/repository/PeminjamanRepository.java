package agile.metamoney.repository;

import agile.metamoney.entity.Peminjaman;
import agile.metamoney.entity.constant.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, String>{
//    @Query(value = "SELECT * FROM t_peminjaman p WHERE p.id_peminjaman=?1 AND p.status=?2", nativeQuery = true)
//    Optional<Peminjaman> findFirstByIdAndStatus(String id,String status);

    Optional<Peminjaman> findFirstByIdAndStatus(String id,EStatus status);
}
