package visang.dataplatform.dataportal.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.datamap.QueryResponseDataMap;
import visang.dataplatform.dataportal.dto.response.datamap.DataMapDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.DataMapService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("datamap")
@Api(tags = { "DataMap API" }, description = "데이터 맵 API")
public class DataMapController {

    private final DataMapService dataMapService;

    @Operation(description = "데이터 맵 - 대분류 단위까지의 데이터 보여주기")
    @GetMapping("category/main")
    public ResponseDto<String> getMapMainData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapService.getMapMainData();
        String result = makeMapData(list, true);
        return ResponseUtil.SUCCESS("데이터 맵 대분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(description = "데이터 맵 - 중분류 단위까지의 데이터 보여주기")
    @GetMapping("category/sub")
    public ResponseDto<String> getMapSubData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapService.getMapSubData();
        String result = makeMapData(list, false);
        return ResponseUtil.SUCCESS("데이터 맵 중분류 단위까지의 데이터 조회에 성공하였습니다.", result);
    }

    @Operation(description = "데이터 맵 - 주요 데이터 셋 이름 보여주기")
    @GetMapping("dataset")
    public ResponseDto<List<String>> getMapSelectedData() {
        List<String> result = dataMapService.getPrimaryDataset();
        return ResponseUtil.SUCCESS("데이터 맵 주요 데이터 셋 조회에 성공하였습니다.", result);

    }

    private static String makeMapData(List<QueryResponseDataMap> list, Boolean isMain) throws JsonProcessingException {
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
            DataMapDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);

            if (isMain){
                serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId, 1);
            } else {
                DataMapDto mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
                mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, 1);
            }

        }

        return convertMapToJson(rootNode);
    }
    private static String convertMapToJson(DataMapDto rootNode) throws JsonProcessingException {
        // Map 형태 데이터를 String으로 변환
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(rootNode);

        // "loc": null와 "children" : null 인 부분을 String 상에서 제거
        String locRemoved = json.replaceAll("\"loc\"\\s*:\\s*null(,)?", "");
        String childrenRemoved = locRemoved.replaceAll("\"children\"\\s*:\\s*null(,)?", "");

        // String을 JsonObject로 파싱할 때, 끝 부분에 따라오는 콤마들을 제거해줘야 에러가 안남
        String cleanedJsonString = removeTrailingCommas(childrenRemoved);
        return cleanedJsonString;

    }

    private static String removeTrailingCommas(String jsonString) {
        Pattern pattern = Pattern.compile(",(?=\\s*\\})|,(?=\\s*\\])");
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.replaceAll("");
    }

}
