package agile.metamoney.service;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.model.request.PembayaranRequest;
import agile.metamoney.model.response.GetPembayaranResponse;
import agile.metamoney.model.response.PembayaranResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PembayaranService {
 PembayaranResponse createNewPembayaran (PembayaranRequest request);

// GetPembayaranResponse getPembayaranByIdPeminjaman(PembayaranRequest request, String idPeminjaman);

 void setPembayaran(String idProduct, DetailPeminjaman idPeminjaman);

 List<GetPembayaranResponse> getListPembayaran(String idPeminjaman);

}
