package agile.metamoney.repository;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.entity.Pembayaran;
import agile.metamoney.entity.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, String> {
    List<Pembayaran> findPembayaranByIdPeminjaman(DetailPeminjaman iddetailPeminjaman);
}
