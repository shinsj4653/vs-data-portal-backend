package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.request.metadata.MetaDataRequest;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.dto.response.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.dto.response.metadata.SubCategoryDto;
import visang.dataplatform.dataportal.dto.response.metadata.TableMetaInfoDto;
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

    @Operation(summary = "대분류 기준 테이블 메타 데이터 정보 조회 API", description = "데이터 맵에서 대분류 정보를 클릭하거나 메타 데이터 정보 메뉴에서 서비스 내에 있는 대분류 정보를 클릭 시, 그 서비스 내의 대분류 영역안에 속하는 중분류 카테고리 정보들을 반환해주는 API")
    @PostMapping("category/main")
    public ResponseDto<Map<String, List<SubCategoryDto>>> getMetaDataWithMainCategory(@RequestBody MetaDataRequest metaDataMain) {
        List<QueryResponseMeta> result = metaDataService.getMetaDataWithMainCategory(metaDataMain.getService_name(), metaDataMain.getMain_category_name());

        if(result.size() == 0){
            return ResponseUtil.FAILURE("서비스 명, 혹은 대분류 명을 다시 한 번 확인해주시길 바랍니다.", null);
        }else {
            return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스와 대분류에 해당하는 중분류 정보들을 가져오는데 성공했습니다.", makeCategoryTree(result));

        }
    }

    @Operation(summary = "중분류 기준 테이블 메타 데이터 정보 조회 API", description = "데이터 맵에서 중분류 정보를 클릭하거나 메타 데이터 메뉴에서 각 대분류 내의 중분류 정보를 클릭하면, 해당 중분류 영역안에 속하는 테이블 메타 데이터 정보들을 모두 가져오는 API")
    @PostMapping("category/sub")
    public ResponseDto<Map<String, List<TableMetaInfoDto>>> getMetaDataWithSubCategory(@RequestBody MetaDataRequest metaDataSub) {
        List<QueryResponseMeta> result = metaDataService.getMetaDataWithSubCategory(metaDataSub.getService_name(), metaDataSub.getMain_category_name(), metaDataSub.getSub_category_name());

        if(result.size() == 0){
            return ResponseUtil.FAILURE("서비스 명, 대분류 명, 혹은 소분류 명을 다시 한 번 확인해주시길 바랍니다.", null);
        }else {
            return ResponseUtil.SUCCESS("메타 데이터 정보 중, 서비스의 대분류와 소분류에 해당하는 메타 데이터 정보들을 가져오는데 성공했습니다.", makeMetaInfoTree(result));
        }
    }
    private Map<String, List<SubCategoryDto>> makeCategoryTree(List<QueryResponseMeta> result) {

        Map<String, List<SubCategoryDto>> treeData = new HashMap<>();

        List<SubCategoryDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            SubCategoryDto subData = new SubCategoryDto(q.getSub_category_id(), q.getSub_category_name());
            list.add(subData);
        }
        treeData.put(result.get(0).getMain_category_name(), list);
        return treeData;

    }

    private Map<String, List<TableMetaInfoDto>> makeMetaInfoTree(List<QueryResponseMeta> result) {

        Map<String, List<TableMetaInfoDto>> treeData = new HashMap<>();

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_name(), q.getTable_id(), q.getTable_comment());
            list.add(metaData);
        }
        treeData.put(result.get(0).getSub_category_name(), list);
        return treeData;
    }


}
