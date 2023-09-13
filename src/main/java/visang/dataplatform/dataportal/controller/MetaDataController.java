package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.metadata.TableColumnDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseTableColumnInfo;
import visang.dataplatform.dataportal.model.request.metadata.*;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.service.MetaDataService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("metadata")
@Api(tags = {"MetaDataInfo API"}, description = "메타데이터 정보 API")
public class MetaDataController {

    private final MetaDataService metaDataService;

    @Operation(summary = "서비스 기준 대분류 데이터 셋 정보 조회 API", description = "각 서비스에 해당하는 대분류 카테고리 이름 정보를 메타 정보 테이블에 많이 존재하는 기준으로 반환해주는 API")
    @PostMapping("dataset/main")
    public ResponseDto<List<String>> getMainDataset(@RequestBody MainDatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getMainDataset(request.getService_name(), limit);
        return ResponseUtil.SUCCESS("서비스에 따른 대분류 데이터 셋 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "서비스 기준 중분류 데이터 셋 정보 조회 API", description = "각 서비스 및 대분류 카테고리에 해당하는 중분류 카테고리 이름 정보를 메타 정보 테이블에 많이 존재하는 기준으로 반환해주는 API")
    @PostMapping("dataset/sub")
    public ResponseDto<List<String>> getSubDataset(@RequestBody SubDatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getSubDataset(request.getService_name(), request.getMain_category_name(), limit);
        return ResponseUtil.SUCCESS("서비스에 따른 중분류 데이터 셋 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "테이블 메타 데이터 정보 조회 API", description = "데이터 맵에서 대분류 혹은 중분류 데이터셋을 클릭하거나 메타 데이터 메뉴에서 대분류와 중분류 데이터 셋을 선택하면, 해당되는 테이블 메타 데이터 정보들을 모두 가져오는 API")
    @PostMapping("tableinfo")
    public ResponseDto<List<TableMetaInfoDto>> getMetaDataWithSubCategory(@RequestBody MetaDataRequest metaDataSub) {
        List<TableMetaInfoDto> result = metaDataService.getMetaDataWithSubCategory(metaDataSub.getService_name(), metaDataSub.getMain_category_name(), metaDataSub.getSub_category_name());
        return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스의 대분류와 소분류에 해당하는 메타 데이터 정보들을 가져오는데 성공했습니다.", result);
    }

    @Operation(summary = "검색 조건 별 검색 결과 조회 API", description = "메타 데이터 정보 페이지 내에서 테이블ID 혹은 테이블명으로 검색 시 해당 키워드에 맞는 메타 데이터 정보들을 반환해주는 API")
    @PostMapping("search/tableinfo")
    public ResponseDto<List<TableSearchDto>> getTableSearchResult(@RequestBody TableSearchRequest req) {
        List<TableSearchDto> result = metaDataService.getTableSearchResult(req.getService_name(), req.getSearch_condition(), req.getTable_keyword(), req.getPage_no(), req.getAmount_per_page());
        return ResponseUtil.SUCCESS("테이블ID 혹은 테이블명으로 검색한 결과를 조회 성공했습니다.", result);
    }

    @Operation(summary = "메타 테이블의 컬럼 정보 조회 API", description = "메타 데이터 정보 페이지 내에서 테이블ID 혹은 테이블명으로 검색 시 해당 키워드에 맞는 메타 데이터 정보들을 반환해주는 API")
    @PostMapping("tablecolumninfo")
    public ResponseDto<List<TableColumnDto>> getTableColumnInfo(@RequestBody TableColumnInfoRequest req) {
        List<TableColumnDto> result = metaDataService.getTableColumnInfo(req.getTable_id());
        return ResponseUtil.SUCCESS("메타 데이터 테이블 컬럼명을 조회 성공했습니다.", result);
    }

    @Operation(summary = "비상교육 전체 메타 데이터 검색 API", description = "메타 테이블 데이터 검색 기준인 table_id, table_comment, 그리고 small_clsf_name 중 하나라도 검색 키워드를 포함하고 있을 시, 해당 결과 반환해주는 API")
    @GetMapping("search/total")
    public ResponseDto<List<TableSearchDto>> getTotalTableSearchResult(@RequestParam(value = "keyword") String keyword) {
        List<TableSearchDto> result = metaDataService.getTotalTableSearchResult(keyword);

        return ResponseUtil.SUCCESS("메타 테이블 전체 검색 결과 조회 성공했습니다.", result);
    }


}
