package visang.dataplatform.dataportal.model.dto.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "데이터 조직도 트리 노드 DTO")
public class DataOrgDto {
    @Schema(description = "이름")
    public String name;

    @Schema(description = "색상")
    public String color;

    @Schema(description = "id 값")
    public String id;

    @Schema(description = "자식 정보")
    private List<DataOrgDto> children = new ArrayList<>();
    public DataOrgDto(String name, String color, String id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public DataOrgDto findOrCreateChild(String name, String color, String id) {
        return children.stream()
                .filter(child -> child.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    DataOrgDto child = new DataOrgDto(name, color, id);
                    children.add(child);
                    return child;
                });
    }
}
