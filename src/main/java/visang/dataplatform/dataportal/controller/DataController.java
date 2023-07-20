package visang.dataplatform.dataportal.controller;
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
import visang.dataplatform.dataportal.dto.response.Result;
import visang.dataplatform.dataportal.service.DataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("CirclePack")
@Api(tags = { "DataMap API" }, description = "비상교육 데이터 맵 API")
public class DataController {

    private final DataService dataService;

    @Operation(description = "GET 요청 테스트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("test")
    public Result getTest() {
        return new Result("Hello World!!");
    }

    @Operation(description = "데이터 맵에 필요한 정보 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DataMapDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("getCirclePackData")
    public DataMapDto getChartData() {
        List<DataByCategoryDto> list = dataService.getChartData();
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
            DataMapDto mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
            DataMapDto subSubjectNode = mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, loc);

            rootNode.addChild(companyNode);

        }

        return rootNode;
    }
}
