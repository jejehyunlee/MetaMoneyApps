package agile.metamoney.controller;

import agile.metamoney.entity.Product;
import agile.metamoney.model.request.ProductRequest;
import agile.metamoney.model.request.UpdateProductRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.ProductResponse;
import agile.metamoney.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request){
        ProductResponse productResponse = productService.createProduct(request);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created product")
                .data(productResponse)
                .build();
        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id){
        Product product = productService.getById(id);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get id product")
                .data(product)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @GetMapping()
    public ResponseEntity<?> getListProduct(){
        List<ProductResponse> allProducts = productService.getAllProducts();
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all product")
                .data(allProducts)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest product){
        ProductResponse productResponse = productService.update(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update product")
                        .data(productResponse)
                        .build());
    }

    @DeleteMapping("/{idAdmin}/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id, @PathVariable("idAdmin") String idAdmin){
        productService.deleteById(id, idAdmin);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete product")
                        .data("Successfully delete product")
                        .build());
    }



}
