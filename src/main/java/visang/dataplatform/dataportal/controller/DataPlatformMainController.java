package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import visang.dataplatform.dataportal.model.dto.dpmain.DatasetSearchDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRankDto;
import visang.dataplatform.dataportal.model.request.dpmain.DatasetSearchRequest;
import visang.dataplatform.dataportal.model.request.metadata.TableSearchRankRequest;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.DataPlatformMainService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("apis/dpmain")
@Api(tags = { "DataPlatform Main API" }, description = "데이터 포털 메인 API")
public class DataPlatformMainController {

    private final DataPlatformMainService dataPlatformMainService;

    @Operation(summary = "메인 화면에서 서비스 명 또는 데이터셋 명으로 검색 후 해당 결과 조회 API", description = "메인 화면에서 서비스 명 또는 데이터 셋 명으로 검색 시, 해당 검색 키워드에 맞는 서비스와 데이터 셋들을 반환해주는 API")
    @PostMapping("search/service-dataset")
    public ResponseDto<List<DatasetSearchDto>> getServiceList(@Valid @RequestBody DatasetSearchRequest req) {
        List<DatasetSearchDto> result = dataPlatformMainService.getServiceList(req.getKeyword(), req.getPage_no(), req.getAmount_per_page());
        return ResponseUtil.SUCCESS("검색 키워드에 맞는 서비스 및 데이터 셋 목록 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "검색어 실시간 순위 집계 API", description = "메타 테이블 데이터 검색 키워드의 실시간 검색 횟수 순위를 반환해주는 API")
    @PostMapping("search/rank")
    public ResponseDto<List<TableSearchKeywordRankDto>> getTableSearchRank(@RequestBody TableSearchRankRequest request) {
        List<TableSearchKeywordRankDto> result = dataPlatformMainService.getTableSearchRank(request);
        return ResponseUtil.SUCCESS("특정 시간대의 검색어 순위 집계에 성공했습니다.", result);
    }
}
