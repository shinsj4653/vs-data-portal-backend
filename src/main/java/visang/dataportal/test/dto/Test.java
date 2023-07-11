package visang.dataportal.test.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Test {

    @Id @GeneratedValue
    private Long id;

    private String testName;
    private int testNum;

    public void createTest(String testName, int testNum) {
        this.testName = testName;
        this.testNum = testNum;
    }

}
