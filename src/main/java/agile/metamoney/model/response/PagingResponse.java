package agile.metamoney.model.response;

import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PagingResponse {
    private Integer currentPage;
    private Integer totalPage;
    private Integer size;

}
