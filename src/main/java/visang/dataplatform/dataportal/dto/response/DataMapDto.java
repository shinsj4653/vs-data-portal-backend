package visang.dataplatform.dataportal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "데이터 맵 응답 DTO")
public class DataMapDto {

    @Schema(description = "이름")
    private String name;

    @Schema(description = "색상")
    private String color;

    @Schema(description = "id값")
    private String id;

    @Schema(description = "자식 정보")
    private List<DataMapDto> children;

    @Schema(description = "메타 테이블 갯수")
    private int loc;

    public DataMapDto(String name, String color, String id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.children = new ArrayList<>();
        this.loc = -1;
    }

    public DataMapDto(String name, String color, String id, int loc) {
        this(name, color, id);
        this.loc = loc;
    }

    public void addChild(DataMapDto child) {
        children.add(child);
    }

    public DataMapDto findOrCreateChild(String name, String color, String id) {
        return children.stream()
                .filter(child -> child.name.equals(name) && child.id.equals(id))
                .findFirst()
                .orElseGet(() -> {
                    DataMapDto child = new DataMapDto(name, color, id);
                    children.add(child);
                    return child;
                });
    }

    public DataMapDto findOrCreateChild(String name, String color, String id, int loc) {
        return children.stream()
                .filter(child -> child.name.equals(name) && child.id.equals(id) && child.loc == loc)
                .findFirst()
                .orElseGet(() -> {
                    DataMapDto child = new DataMapDto(name, color, id, loc);
                    children.add(child);
                    return child;
                });
    }
}
