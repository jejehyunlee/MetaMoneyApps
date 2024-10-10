package agile.metamoney.service.impl;

import agile.metamoney.entity.DetailPeminjaman;
import agile.metamoney.entity.Peminjaman;
import agile.metamoney.entity.Product;
import agile.metamoney.model.request.PeminjamanRequest;
import agile.metamoney.repository.DetailPeminjamanRepository;
import agile.metamoney.service.DetailPeminjamanService;
import agile.metamoney.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailPeminjamanServiceImpl implements DetailPeminjamanService {

    private final DetailPeminjamanRepository detailPeminjamanRepository;
    private final ProductService productService;

    @Override
    public DetailPeminjaman createDetailPeminjaman(PeminjamanRequest request, Peminjaman peminjaman) {
        Product currentProduct = productService.getById(request.getIdProduct());
        DetailPeminjaman detailPeminjaman = DetailPeminjaman.builder()
                .sisaPinjaman(Math.round(currentProduct.getJumlah() + (currentProduct.getJumlah() * currentProduct.getBunga())))
                .sisaTenor(currentProduct.getTenor())
                .idProduct(currentProduct)
                .idPeminjaman(peminjaman)
                .build();
        return detailPeminjamanRepository.save(detailPeminjaman);
    }
}
