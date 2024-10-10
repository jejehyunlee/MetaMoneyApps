package agile.metamoney.entity.constant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum EPayment {
    BRI,
    CIMB,
    MANDIRI,
    BCA,
    BNI;

    public static EPayment get(String value){
        for (EPayment ePayment : EPayment.values()) {
            if (ePayment.name().equalsIgnoreCase(value)) return ePayment;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EPayment not found");
    }
}
