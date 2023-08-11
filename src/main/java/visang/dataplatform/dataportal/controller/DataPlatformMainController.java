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

    @Operation(summary = "메인 화면에서 서비스 명 또는 데이터셋 명으로 검색 후 해당 결과 조회 API", description = "메인 화면에서 서비스 명 또는 데이터 셋 명으로 검색 시, 해당 검색 키워드에 맞는 서비스와 데이터 셋들을 반환해주는 API")
    @PostMapping("search/service-dataset")
    public ResponseDto<List<DatasetSearchDto>> getServiceList(@RequestBody DatasetSearchRequest req) {
        List<DatasetSearchDto> result = dataPlatformMainService.getServiceList(req.getKeyword());
        return ResponseUtil.SUCCESS("검색 키워드에 맞는 서비스 및 데이터 셋 목록 조회에 성공하였습니다.", result);
    }
}
