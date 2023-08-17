package visang.dataplatform.dataportal.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.DataMapService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("datamap")
@Api(tags = { "DataMap API" }, description = "데이터 맵 API")
public class DataMapController {

    private final DataMapService dataMapService;

    @Operation(summary = "데이터 맵 대분류 정보 조회 API", description = "데이터 맵에 필요한 정보를 “대분류 카테고리” 단위까지 모두 가져오는 API")
    @GetMapping("category/main")
    public ResponseDto<Map<String, String>> getMapMainData() throws JsonProcessingException {
        Map<String, String> result = dataMapService.getMapMainData();
        return ResponseUtil.SUCCESS("데이터 맵 대분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "데이터 맵 중분류 정보 조회 API", description = "데이터 맵에 필요한 정보를 “중분류 카테고리” 단위까지 모두 가져오는 API")
    @GetMapping("category/sub")
    public ResponseDto<Map<String, String>> getMapSubData() throws JsonProcessingException {
        Map<String, String> result = dataMapService.getMapSubData();
        return ResponseUtil.SUCCESS("데이터 맵 중분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "데이터 맵 데이터 셋 정보 조회 API", description = "데이터 맵 화면의 대분류 기준 주요 데이터 셋 정보를 반환해주는 API")
    @GetMapping("dataset/all")
    public ResponseDto<List<String>> getAllDataset() {
        List<String> result = dataMapService.getAllDataset();
        return ResponseUtil.SUCCESS("데이터 맵 데이터 셋 조회에 성공하였습니다.", result);
    }
}
