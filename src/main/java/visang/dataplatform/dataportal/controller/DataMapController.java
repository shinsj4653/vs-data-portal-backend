package visang.dataplatform.dataportal.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.datamap.DataMapDto;
import visang.dataplatform.dataportal.model.entity.datamap.QueryResponseDataMap;
import visang.dataplatform.dataportal.response.common.ResponseDto;
import visang.dataplatform.dataportal.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.DataMapService;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("datamap")
@Api(tags = { "DataMap API" }, description = "데이터 맵 API")
public class DataMapController {

    private final DataMapService dataMapService;
    static ObjectMapper mapper = new ObjectMapper();

    @Operation(summary = "데이터 맵 대분류 정보 조회 API", description = "비상교육 데이터 맵 메뉴를 클릭하였을 때 보여지는 데이터 맵에 필요한 데이터를 “대분류 카테고리” 단위까지 모두 가져오는 API")
    @GetMapping("category/main")
    public ResponseDto<Map<String, String>> getMapMainData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapService.getMapMainData();
        Map<String, String> result = refactorMapData(list, true);
        return ResponseUtil.SUCCESS("데이터 맵 대분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "데이터 맵 중분류 정보 조회 API", description = "비상교육 데이터 맵 메뉴를 클릭하였을 때 보여지는 데이터 맵에 필요한 데이터를 “중분류 카테고리” 단위까지 모두 가져오는 API")
    @GetMapping("category/sub")
    public ResponseDto<Map<String, String>> getMapSubData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapService.getMapSubData();
        Map<String, String> result = refactorMapData(list, false);
        return ResponseUtil.SUCCESS("데이터 맵 중분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "데이터 맵 주요 데이터 셋 정보 조회 API", description = "비상교육 데이터 맵 메뉴를 클릭하였을 때 보여지는 데이터 맵 화면에서 주요 데이터 셋의 이름 정보를 반환해주는 API")
    @GetMapping("dataset")
    public ResponseDto<List<String>> getMapSelectedData() {
        List<String> result = dataMapService.getPrimaryDataset();
        return ResponseUtil.SUCCESS("데이터 맵 주요 데이터 셋 조회에 성공하였습니다.", result);
    }

    static Map<String, String> refactorMapData(List<QueryResponseDataMap> list, Boolean isMain) throws JsonProcessingException {
        int id = 0;

        DataMapDto rootNode = new DataMapDto("비상교육", "#00b2e2", "node-" + (id++));

        for (QueryResponseDataMap data : list) {
            String companyName = data.getCompany_name();
            String companyColor = data.getCompany_color();
            String companyId = "node-" + (id++);

            String serviceName = data.getService_name();
            String serviceColor = data.getService_color();
            String serviceId = "node-" + (id++);

            String mainSubjectName = data.getMain_category_name();
            String mainSubjectColor = data.getMain_category_color();
            String mainSubjectId = "node-" + (id++);

            String subSubjectName = data.getSub_category_name();
            String subSubjectColor = data.getSub_category_color();
            String subSubjectId = "node-" + (id++);

            DataMapDto companyNode = rootNode.findOrCreateChild(companyName, companyColor, companyId);

            if (isMain){
                if (mainSubjectName != null) {
                    DataMapDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);
                    serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId, 1);
                } else {
                    companyNode.findOrCreateChild(serviceName, serviceColor, serviceId, 1);
                }
            } else {
                if (mainSubjectName != null) {
                    DataMapDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);

                    if (subSubjectName != null) {
                        DataMapDto mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
                        mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, 1);
                    } else {
                        serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId, 1);
                    }
                } else{
                    companyNode.findOrCreateChild(serviceName, serviceColor, serviceId, 1);
                }
            }

        }

        // Map 형태 데이터를 String으로 변환해서 파라미터로 넘겨주기
        return convertMapToJson(mapper.writeValueAsString(rootNode));
    }
    static Map<String, String> convertMapToJson(String json) throws JsonProcessingException {
        // "loc": null와 "children" : null 인 부분을 String 상에서 제거
        String locRemoved = json.replaceAll("\"loc\"\\s*:\\s*null(,)?", "");
        String childrenRemoved = locRemoved.replaceAll("\"children\"\\s*:\\s*\\[\\]\\s*(,)?", "");

        // String을 JsonObject로 파싱할 때, 끝 부분에 따라오는 콤마들을 제거해줘야 에러가 안남
        String cleanedJsonString = removeTrailingCommas(childrenRemoved);
        Map<String, String> map = mapper.readValue(cleanedJsonString, Map.class);

        return map;

    }

    static String removeTrailingCommas(String jsonString) {
        Pattern pattern = Pattern.compile(",(?=\\s*\\})|,(?=\\s*\\])");
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.replaceAll("");
    }

}
