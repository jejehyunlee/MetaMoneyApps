package agile.metamoney.service;

import agile.metamoney.entity.constant.EStatus;
import agile.metamoney.model.request.PeminjamanRequest;
import agile.metamoney.model.request.UpdateStatusRequest;
import agile.metamoney.model.response.PeminjamanResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PeminjamanService {
    PeminjamanResponse createPinjamanBaru (PeminjamanRequest peminjamanRequest, MultipartFile ftTtd) throws IOException;

    PeminjamanResponse updateStatusPeminjaman (UpdateStatusRequest request);
}
