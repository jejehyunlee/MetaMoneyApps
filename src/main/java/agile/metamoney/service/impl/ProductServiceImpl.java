package agile.metamoney.service.impl;

import agile.metamoney.entity.Admin;
import agile.metamoney.entity.Product;
import agile.metamoney.model.request.ProductRequest;
import agile.metamoney.model.request.UpdateProductRequest;
import agile.metamoney.model.response.ProductResponse;
import agile.metamoney.repository.AdminRepository;
import agile.metamoney.repository.ProductRepository;
import agile.metamoney.service.AdminService;
import agile.metamoney.service.ProductService;
import agile.metamoney.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ValidationService validationService;
    private final AdminService adminService;
    private final AdminRepository adminRepository;

    private static ProductResponse toProductResponse(Product request){
        return ProductResponse.builder()
                .id(request.getId())
                .nama(request.getNama())
                .jumlah(request.getJumlah())
                .tenor(request.getTenor())
                .bunga(request.getBunga())
                .denda(request.getDenda())
                .build();
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        validationService.validate(request);
        Admin admin = adminRepository.findById(request.getIdAdmin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Create Product Failed"));

        try {
            Product product = Product.builder()
                    .nama(request.getNama())
                    .jumlah(request.getJumlah())
                    .tenor(request.getTenor())
                    .bunga(request.getBunga())
                    .denda(request.getDenda())
                    .build();
            productRepository.save(product);

            return toProductResponse(product);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "product already exist");
        }
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> productResponses = productRepository.findAll().stream().map(product -> toProductResponse(product)).collect(Collectors.toList());
        return productResponses;
    }

    @Override
    public ProductResponse update(UpdateProductRequest request) {
        validationService.validate(request);
        Admin admin = adminRepository.findById(request.getIdAdmin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Update Product Failed"));

        Product productValidation = productRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Create Product Failed"));

        try {
            Product product = getById(request.getId());
            product.setNama(request.getNama());
            product.setJumlah(request.getJumlah());
            product.setTenor(request.getTenor());
            product.setBunga(request.getBunga());
            product.setDenda(request.getDenda());

            return toProductResponse(productRepository.save(product));
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "product already exist");
        }
    }

    @Override
    public void deleteById(String id, String idAdmin) {
        validationService.validate(idAdmin);
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Update Product Failed"));

        productRepository.deleteById(id);
    }


}










