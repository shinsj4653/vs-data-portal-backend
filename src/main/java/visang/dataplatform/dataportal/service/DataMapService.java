package visang.dataplatform.dataportal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.mapper.DataMapMapper;
import visang.dataplatform.dataportal.model.dto.datamap.DataMapDto;
import visang.dataplatform.dataportal.model.entity.datamap.QueryResponseDataMap;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMapService {

    private final DataMapMapper dataMapMapper;
    public static ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> getMapMainData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapMapper.getMapMainData();
        Map<String, String> result = refactorMapData(list, true);
        return result;
    }
    public Map<String, String> getMapSubData() throws JsonProcessingException {
        List<QueryResponseDataMap> list = dataMapMapper.getMapSubData();
        Map<String, String> result = refactorMapData(list, false);
        return result;
    }
    public List<String> getAllDataset() {
        return dataMapMapper.getAllDataset();
    }

    // 리스트 형태의 데이터를 트리 구조로 변환해주는 함수
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

    // String 데이터를 Map 구조의 Json Object 데이터로 변환해주는 함수
    public static Map<String, String> convertMapToJson(String json) throws JsonProcessingException {
        // "loc": null와 "children" : null 인 부분을 String 상에서 제거
        String locRemoved = json.replaceAll("\"loc\"\\s*:\\s*null(,)?", "");
        String childrenRemoved = locRemoved.replaceAll("\"children\"\\s*:\\s*\\[\\]\\s*(,)?", "");

        // String을 Json Object로 파싱할 때, 끝 부분에 따라오는 콤마들을 제거해줘야 에러가 안남
        String cleanedJsonString = removeTrailingCommas(childrenRemoved);
        Map<String, String> map = mapper.readValue(cleanedJsonString, Map.class);

        return map;

    }

    // Json Object 로 파싱할 때 오류를 낼 수 있는 콤마부분을 삭제해주는 함수
    static String removeTrailingCommas(String jsonString) {
        Pattern pattern = Pattern.compile(",(?=\\s*\\})|,(?=\\s*\\])");
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.replaceAll("");
    }

}
