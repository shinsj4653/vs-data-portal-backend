package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.request.metadata.DatasetRequest;
import visang.dataplatform.dataportal.request.metadata.MetaDataRequest;
import visang.dataplatform.dataportal.response.common.ResponseDto;
import visang.dataplatform.dataportal.response.common.ResponseUtil;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.model.dto.metadata.SubCategoryDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.service.MetaDataService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("metadata")
@Api(tags = { "MetaDataInfo API" }, description = "메타데이터 정보 API")
public class MetaDataController {

    private final MetaDataService metaDataService;

    @Operation(summary = "서비스에 따른 대분류 데이터 셋 정보 조회 API", description = "비상교육 데이터 맵 메뉴를 클릭하였을 때 보여지는 데이터 맵 화면에서 모든 주요 데이터 셋의 이름 정보를 반환해주는 API")
    @PostMapping("dataset/main")
    public ResponseDto<List<String>> getMainDataset(@RequestBody DatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getMainDataset(request.getService_name(), limit);
        return ResponseUtil.SUCCESS("서비스에 따른 대분류 데이터 셋 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "서비스에 따른 중분류 데이터 셋 정보 조회 API", description = "비상교육 데이터 맵 메뉴를 클릭하였을 때 보여지는 데이터 맵 화면에서 모든 주요 데이터 셋의 이름 정보를 반환해주는 API")
    @PostMapping("dataset/sub")
    public ResponseDto<List<String>> getSubDataset(@RequestBody DatasetRequest request, @RequestParam(required = false, value = "limit") Integer limit) {
        List<String> result = metaDataService.getSubDataset(request.getService_name(), limit);
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
        List<QueryResponseMeta> result = metaDataService.getMetaDataWithSubCategory(metaDataSub.getService_name(), metaDataSub.getMain_category_name(), metaDataSub.getSub_category_name(), metaDataSub.getAmount(), metaDataSub.getPage_no());
        if (metaDataSub.getPage_no() <= 0) {
            return ResponseUtil.FAILURE("페이지 번호는 1 이상이어야 합니다.", null);
        } else
            return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스의 대분류와 소분류에 해당하는 메타 데이터 정보들을 가져오는데 성공했습니다.", makeMetaInfoTree(result));
    }
    private List<SubCategoryDto> makeCategoryTree(List<QueryResponseMeta> result) {

        List<SubCategoryDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            SubCategoryDto subData = new SubCategoryDto(q.getSub_category_id(), q.getSub_category_name());
            list.add(subData);
        }
        return list;

    }

    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result) {

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_name(), q.getTable_comment(), q.getSmall_clsf_name());
            list.add(metaData);
        }
        return list;
    }


}
