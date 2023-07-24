package visang.dataplatform.dataportal.controller;
import com.google.gson.*;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.DataByCategoryDto;
import visang.dataplatform.dataportal.dto.response.DataMapDto;
import visang.dataplatform.dataportal.service.DataMapService;
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
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DataMapDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("category/main")
    public JsonObject getMapMainData() {
        List<DataByCategoryDto> list = dataMapService.getMapMainData();
        return makeMapData(list, true);
    }

    @Operation(description = "데이터 맵 - 중분류 단위까지의 데이터 보여주기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DataMapDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("category/sub")
    public JsonObject getMapSubData() {
        List<DataByCategoryDto> list = dataMapService.getMapSubData();
        return makeMapData(list, false);
    }

    private static JsonObject makeMapData(List<DataByCategoryDto> list, Boolean isMain) {
        Map<String, DataMapDto> companyMap = new HashMap<>();
        DataMapDto rootNode = new DataMapDto("비상교육", "#00b2e2", "node-parent");

        for (DataByCategoryDto data : list) {
            String companyName = data.getCompany_name();
            String companyColor = data.getCompany_color();
            String companyId = String.valueOf(data.getCompany_id());

            String serviceName = data.getService_name();
            String serviceColor = data.getService_color();
            String serviceId = String.valueOf(data.getService_id());

            String mainSubjectName = data.getMain_category_name();
            String mainSubjectColor = data.getMain_category_color();
            String mainSubjectId = String.valueOf(data.getMain_category_id());

            String subSubjectName = data.getSub_category_name();
            String subSubjectColor = data.getSub_category_color();
            String subSubjectId = String.valueOf(data.getSub_category_id());
            int loc = data.getLoc();

            DataMapDto companyNode = companyMap.computeIfAbsent(companyName, name -> new DataMapDto(companyName, companyColor, companyId));
            DataMapDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);

            if (isMain){
                serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId, loc);
            } else {
                DataMapDto mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
                mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, loc);
            }

            rootNode.addChild(companyNode);

        }

        return convertMapToJson(rootNode);
    }

    private static JsonObject convertMapToJson(DataMapDto rootNode) {
        // Map 형태 데이터를 String으로 변환
        Gson gsonBuilder = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String updatedJsonData = gsonBuilder.toJson(rootNode);

        // "loc": null와 "children" : null 인 부분을 String 상에서 제거
        String locRemoved = updatedJsonData.replaceAll("\"loc\"\\s*:\\s*null(,)?", "");
        String childrenRemoved = locRemoved.replaceAll("\"children\"\\s*:\\s*null(,)?", "");

        // String을 JsonObject로 파싱할 때, 끝 부분에 따라오는 콤마들을 제거해줘야 에러가 안남
        String cleanedJsonString = removeTrailingCommas(childrenRemoved);

        // JsonParser를 가지고 JsonObject 형태로 String을 변환해줌
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(cleanedJsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return jsonObject;
    }

    private static String removeTrailingCommas(String jsonString) {
        Pattern pattern = Pattern.compile(",(?=\\s*\\})|,(?=\\s*\\])");
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.replaceAll("");
    }


}
