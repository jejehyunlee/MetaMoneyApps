package agile.metamoney.entity.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum EGender {
    PRIA,
    WANITA;

    public static EGender get(String value){
        for (EGender eGender : EGender.values()) {
            if (eGender.name().equalsIgnoreCase(value)) return eGender;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found");
    }
}
