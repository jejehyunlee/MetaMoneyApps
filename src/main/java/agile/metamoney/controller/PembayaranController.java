package agile.metamoney.controller;

import agile.metamoney.model.request.PembayaranRequest;
import agile.metamoney.model.response.CommonResponse;
import agile.metamoney.model.response.GetPembayaranResponse;
import agile.metamoney.model.response.PembayaranResponse;
import agile.metamoney.service.PembayaranService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pembayaran")
public class PembayaranController {
    private final PembayaranService pembayaranService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewPembayaran(@RequestBody PembayaranRequest request){
        PembayaranResponse newPembayaran = pembayaranService.createNewPembayaran(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Login")
                .data(newPembayaran)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);

    }

    @GetMapping(
            path = "/{idDetailPeminjaman}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllPembayaran(@PathVariable(name = "idDetailPeminjaman") String idDetailPeminjaman){
        List<GetPembayaranResponse> listPembayaran = pembayaranService.getListPembayaran(idDetailPeminjaman);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Login")
                .data(listPembayaran)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);


    }
}
