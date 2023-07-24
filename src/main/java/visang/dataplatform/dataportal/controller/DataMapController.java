package visang.dataplatform.dataportal.controller;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.DataByCategoryDto;
import visang.dataplatform.dataportal.dto.response.DataMapDto;
import visang.dataplatform.dataportal.service.DataMapService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("datamap")
@Api(tags = { "DataMap API" }, description = "데이터 맵 API")
public class DataMapController {

    private final DataMapService dataMapService;

    @Operation(description = "데이터 맵 - 대분류 단위까지의 데이터 보여주기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("category/main")
    public String getMapMainData() throws JsonProcessingException {
        List<DataByCategoryDto> list = dataMapService.getMapMainData();
        return makeMapData(list, true);
    }

    @Operation(description = "데이터 맵 - 중분류 단위까지의 데이터 보여주기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("category/sub")
    public String getMapSubData() throws JsonProcessingException {
        List<DataByCategoryDto> list = dataMapService.getMapSubData();
        return makeMapData(list, false);
    }

    @Operation(description = "데이터 맵 - 주요 데이터 셋 이름 보여주기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("dataset")
    public List<String> getMapSelectedData() {
        return dataMapService.getPrimaryDataset();
    }

    private static String makeMapData(List<DataByCategoryDto> list, Boolean isMain) throws JsonProcessingException {
        int id = 0;

        DataMapDto rootNode = new DataMapDto("비상교육", "#00b2e2", "node-" + (id++));

        for (DataByCategoryDto data : list) {
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
            int loc = data.getLoc();

            DataMapDto companyNode = rootNode.findOrCreateChild(companyName, companyColor, companyId);
            DataMapDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);

            if (isMain){
                serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId, loc);
            } else {
                DataMapDto mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
                mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, loc);
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