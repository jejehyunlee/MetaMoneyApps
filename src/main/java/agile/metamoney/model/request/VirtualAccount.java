package agile.metamoney.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VirtualAccount {
    private String channel_code;
    private ChanelProperties channel_properties;

}
