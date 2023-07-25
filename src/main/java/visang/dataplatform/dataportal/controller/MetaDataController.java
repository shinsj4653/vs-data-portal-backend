package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.request.MetaDataMain;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.MetaDataService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("metadata")
@Api(tags = { "MetaDataInfo API" }, description = "메타데이터 정보 API")
public class MetaDataController {

    private final MetaDataService metaDataService;

    @Operation(description = "메타 데이터 정보 - 대분류 클릭 시, 중분류에 해당하는 정보 보여주기")
    @PostMapping("category/main")
    public ResponseDto getMapSelectedData(@RequestBody MetaDataMain metaDataMain) {
        List<String> result = metaDataService.getMainCategory(metaDataMain.getServiceName(), metaDataMain.getMainCategoryName());

        if(result.size() == 0){
            return ResponseUtil.FAILURE("서비스 명, 혹은 대분류 명을 다시 한 번 확인해주시길 바랍니다.", null);
        }else {
            return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스와 대분류에 해당하는 중분류 정보들을 가져오는데 성공했습니다.", result);

        }
    }


}
