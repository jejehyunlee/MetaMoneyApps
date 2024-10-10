package agile.metamoney.service;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.entity.Peminjaman;
import agile.metamoney.model.request.PeminjamanRequest;

import java.time.LocalDate;

public interface DetailPeminjamanService {
    DetailPeminjaman createDetailPeminjaman (PeminjamanRequest request, Peminjaman peminjaman);
}
