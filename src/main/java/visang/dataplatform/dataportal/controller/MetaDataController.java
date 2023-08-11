package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.request.metadata.MainDatasetRequest;
import visang.dataplatform.dataportal.request.metadata.MetaDataRequest;
import visang.dataplatform.dataportal.request.metadata.SubDatasetRequest;
import visang.dataplatform.dataportal.request.metadata.TableSearchRequest;
import visang.dataplatform.dataportal.response.common.ResponseDto;
import visang.dataplatform.dataportal.response.common.ResponseUtil;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.model.dto.metadata.SubCategoryDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.service.MetaDataService;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("metadata")
@Api(tags = { "MetaDataInfo API" }, description = "메타데이터 정보 API")
public class MetaDataController {

    private final MetaDataService metaDataService;

    @Operation(summary = "서비스에 따른 대분류 데이터 셋 정보 조회 API", description = "각 서비스에 해당하는 대분류 카테고리 이름 정보를 메타 정보 테이블에 많이 존재하는 기준으로 반환해주는 API")
    @PostMapping("dataset/main")
    public ResponseDto<List<String>> getMainDataset(@RequestBody MainDatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getMainDataset(request.getService_name(), limit);
        return ResponseUtil.SUCCESS("서비스에 따른 대분류 데이터 셋 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "서비스에 따른 중분류 데이터 셋 정보 조회 API", description = "각 서비스 및 대분류 카테고리에 해당하는 중분류 카테고리 이름 정보를 메타 정보 테이블에 많이 존재하는 기준으로 반환해주는 API")
    @PostMapping("dataset/sub")
    public ResponseDto<List<String>> getSubDataset(@RequestBody SubDatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getSubDataset(request.getService_name(), request.getMain_category_name(), limit);
        return ResponseUtil.SUCCESS("서비스에 따른 중분류 데이터 셋 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "대분류 기준 테이블 메타 데이터 정보 조회 API", description = "데이터 맵에서 대분류 정보를 클릭하거나 메타 데이터 정보 메뉴에서 서비스 내에 있는 대분류 정보를 클릭 시, 그 서비스 내의 대분류 영역안에 속하는 중분류 카테고리 정보들을 반환해주는 API")
    @PostMapping("category/main")
    public ResponseDto<List<SubCategoryDto>> getMetaDataWithMainCategory(@RequestBody MetaDataRequest metaDataMain) {
        List<QueryResponseMeta> result = metaDataService.getMetaDataWithMainCategory(metaDataMain.getService_name(), metaDataMain.getMain_category_name());
        return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스와 대분류에 해당하는 중분류 정보들을 가져오는데 성공했습니다.", makeCategoryTree(result));
    }

    @Operation(summary = "중분류 기준 테이블 메타 데이터 정보 조회 API", description = "데이터 맵에서 중분류 정보를 클릭하거나 메타 데이터 메뉴에서 각 대분류 내의 중분류 정보를 클릭하면, 해당 중분류 영역안에 속하는 테이블 메타 데이터 정보들을 모두 가져오는 API")
    @PostMapping("category/sub")
    public ResponseDto<List<TableMetaInfoDto>> getMetaDataWithSubCategory(@RequestBody MetaDataRequest metaDataSub) {
        List<QueryResponseMeta> result = metaDataService.getMetaDataWithSubCategory(metaDataSub.getService_name(), metaDataSub.getMain_category_name(), metaDataSub.getSub_category_name());
        return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스의 대분류와 소분류에 해당하는 메타 데이터 정보들을 가져오는데 성공했습니다.", makeMetaInfoTree(result));
    }

    @Operation(summary = "테이블ID 혹은 이름 기준 검색 결과 조회 API", description = "메타 데이터 정보 페이지 내에서 테이블ID 혹은 테이블명으로 검색 시 해당 키워드에 맞는 메타 데이터 정보들을 반환해주는 API")
    @PostMapping("search/tableinfo")
    public ResponseDto<List<TableSearchDto>> getTableSearchResult(@RequestBody TableSearchRequest req) {
        List<TableSearchDto> result = metaDataService.getTableSearchResult(req.getService_name(), req.getTable_keyword());
        return ResponseUtil.SUCCESS("테이블ID 혹은 테이블명으로 검색한 결과를 조회 성공했습니다.",result);
    }

    // QueryResponseMeta에서 SubCategoryDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<SubCategoryDto> makeCategoryTree(List<QueryResponseMeta> result) {

        List<SubCategoryDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            SubCategoryDto subData = new SubCategoryDto(q.getSub_category_id(), q.getSub_category_name());
            list.add(subData);
        }
        return list;

    }

    // QueryResponseMeta에서 TableMetaInfoDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result) {

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_name(), q.getTable_comment(), q.getSmall_clsf_name());
            list.add(metaData);
        }
        return list;
    }

}
