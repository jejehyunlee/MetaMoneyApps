package agile.metamoney.entity.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum EStatus {
    DITERIMA,
    PENDING,
    BELUM_LUNAS,
    LUNAS,
    SUCCEEDED,
    FAILED;

    public static EStatus get(String value){
        for (EStatus eStatus : EStatus.values()) {
            if (eStatus.name().equalsIgnoreCase(value)) return eStatus;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "status not found");
    }
}
