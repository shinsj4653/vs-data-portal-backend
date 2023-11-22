package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Schema(description = "메타 데이터 정보 API 요청 Body")
public class MetaDataRequest {

    @NotEmpty
    @Schema(description = "서비스 명")
    private String service_name;

    @NotEmpty
    @Schema(description = "대분류 명")
    private String main_category_name;

    @NotEmpty
    @Schema(description = "중분류 명")
    private String sub_category_name;

    @PositiveOrZero
    @Schema(description = "페이지 번호")
    private Integer page_no;

    @Positive
    @Schema(description = "페이지 당 갯수")
    private Integer amount_per_page;

}
