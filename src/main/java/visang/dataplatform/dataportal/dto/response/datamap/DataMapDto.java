package visang.dataplatform.dataportal.dto.response.datamap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "데이터 맵 트리 노드 DTO")
public class DataMapDto {
    @Schema(description = "이름")
    public String name;

    @Schema(description = "색상")
    public String color;

    @Schema(description = "id 값")
    public String id;

    @Schema(description = "자식 정보")
    private List<DataMapDto> children = new ArrayList<>();

    @Schema(description = "loc 값")
    private Integer loc;

    public DataMapDto(String name, String color, String id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.loc = null;
    }

    public DataMapDto(String name, String color, String id, Integer loc) {
        this(name, color, id);
        this.loc = loc;
    }

    public DataMapDto findOrCreateChild(String name, String color, String id) {
        return children.stream()
                .filter(child -> child.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    DataMapDto child = new DataMapDto(name, color, id);
                    children.add(child);
                    return child;
                });
    }

    public DataMapDto findOrCreateChild(String name, String color, String id, Integer loc) {
        return children.stream()
                .filter(child -> child.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    DataMapDto child = new DataMapDto(name, color, id, loc);
                    children.add(child);
                    return child;
                });
    }


}
