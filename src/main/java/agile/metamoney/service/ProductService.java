package agile.metamoney.service;

import agile.metamoney.entity.Product;
import agile.metamoney.model.request.ProductRequest;
import agile.metamoney.model.request.UpdateProductRequest;
import agile.metamoney.model.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Product getById(String id);

    List<ProductResponse> getAllProducts();

    ProductResponse update(UpdateProductRequest request);

    void deleteById(String id, String idAdmin);
}
