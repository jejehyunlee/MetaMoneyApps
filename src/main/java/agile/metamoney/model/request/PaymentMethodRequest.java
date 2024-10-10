package agile.metamoney.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaymentMethodRequest {
    private String type;
    private String reusability;
    private String reference_id;
    private VirtualAccount virtual_account;
}
