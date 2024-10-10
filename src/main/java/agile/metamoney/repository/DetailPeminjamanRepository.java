package agile.metamoney.repository;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.entity.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailPeminjamanRepository extends JpaRepository<DetailPeminjaman, String> {
    Optional<DetailPeminjaman> findFirstByIdPeminjaman(Peminjaman peminjaman);
}
