package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import visang.dataplatform.dataportal.model.dto.dpmain.DatasetSearchDto;
import visang.dataplatform.dataportal.request.dpmain.DatasetSearchRequest;
import visang.dataplatform.dataportal.response.common.ResponseDto;
import visang.dataplatform.dataportal.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.DataPlatformMainService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("dpmain")
@Api(tags = { "DataPlatform Main API" }, description = "데이터 포털 메인 API")
public class DataPlatformMainController {

    private final DataPlatformMainService dataPlatformMainService;

    @Operation(summary = "특정 데이터 셋을 포함하고 있는 서비스 명 조회 API", description = "특정 데이터 셋을 검색하였을 때, 그 데이터 셋을 가지고 있는 서비스 명을 반환")
    @PostMapping("search/dataset")
    public ResponseDto<List<DatasetSearchDto>> getServiceList(@RequestBody DatasetSearchRequest req) {
        List<DatasetSearchDto> result = dataPlatformMainService.getServiceList(req.getKeyword());
        return ResponseUtil.SUCCESS("데이터 셋에 따른 서비스 명 조회에 성공하였습니다.", result);
    }
}
