package visang.dataplatform.dataportal.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.*;
import visang.dataplatform.dataportal.service.DataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chart Data API", description = "차트 데이터 API")
public class DataController {

    private final DataService dataService;

    @Operation(summary = "get tests", description = "test 테이블에서 데이터 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TestDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/test/getTestList")
    public Result getTestList() {

        return new Result("Hello World!!");
    }

    @GetMapping("/test/getTestById/{id}")
    public Result getTestById(@PathVariable Long id) {

        TestDto result = dataService.getTestById(id);
        return new Result(result);

    }

    @GetMapping("/chart/getAllData")
    public Result getAllData() {
        List<DataBySubjectDto> list = dataService.getAllData();
        Map<String, Node> companyMap = new HashMap<>();
        Node rootNode = new Node("비상교육", "#00b2e2", "node-parent");

        for (DataBySubjectDto data : list) {
            String companyName = data.getCompany_name() + " Company";
            String companyColor = data.getCompany_color();
            String companyId = String.valueOf(data.getCompany_id());

            String serviceName = data.getService_name();
            String serviceColor = data.getService_color();
            String serviceId = String.valueOf(data.getService_id());

            String mainSubjectName = data.getMain_subject_name();
            String mainSubjectColor = data.getMain_subject_color();
            String mainSubjectId = String.valueOf(data.getMain_subject_id());

            String subSubjectName = data.getSub_subject_name();
            String subSubjectColor = data.getSub_subject_color();
            String subSubjectId = String.valueOf(data.getSub_subject_id());
            int loc = data.getLoc();

            Node companyNode = companyMap.computeIfAbsent(companyName, name -> new Node(companyName, companyColor, companyId));
            Node serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);
            Node mainSubjectNode = serviceNode.findOrCreateChild(mainSubjectName, mainSubjectColor, mainSubjectId);
            Node subSubjectNode = mainSubjectNode.findOrCreateChild(subSubjectName, subSubjectColor, subSubjectId, loc);

            rootNode.addChild(companyNode);

        }

        return new Result(rootNode);
    }
}
