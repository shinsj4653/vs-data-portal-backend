package visang.dataportal.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataBySubjectResult {
    private String name;
    private String color;
    private int id;
    private List children;
}
