package visang.dataplatform.dataportal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Test {

    @Id
    @GeneratedValue
    private Long id;

    private String testName;
    private int testNum;

    public void createTest(String testName, int testNum) {
        this.testName = testName;
        this.testNum = testNum;
    }

}
