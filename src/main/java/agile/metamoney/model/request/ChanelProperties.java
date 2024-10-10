package agile.metamoney.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChanelProperties {
    private String customer_name;
}
