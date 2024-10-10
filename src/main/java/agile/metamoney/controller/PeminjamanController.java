package agile.metamoney.controller;

import agile.metamoney.model.request.PeminjamanRequest;
import agile.metamoney.model.request.ProductRequest;
import agile.metamoney.model.request.UpdateCustomerRequest;
import agile.metamoney.model.request.UpdateStatusRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.PeminjamanResponse;
import agile.metamoney.model.response.ProductResponse;
import agile.metamoney.service.PeminjamanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/peminjaman")
public class PeminjamanController {

    private final PeminjamanService peminjamanService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> createPeminjaman(@RequestParam("data-pinjaman") String jsonObject, MultipartFile ftTtd) throws IOException {
        PeminjamanRequest request =objectMapper.readValue(jsonObject, PeminjamanRequest.class);
        PeminjamanResponse pinjamanBaru = peminjamanService.createPinjamanBaru(request,ftTtd);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created peminjaman")
                .data(pinjamanBaru)
                .build();
        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateStatusPeminjaman(@RequestBody UpdateStatusRequest request){
        PeminjamanResponse updateStatusPeminjaman = peminjamanService.updateStatusPeminjaman(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update status peminjaman")
                .data(updateStatusPeminjaman)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }
}
