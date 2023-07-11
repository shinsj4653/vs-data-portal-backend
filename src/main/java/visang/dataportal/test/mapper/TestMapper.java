package visang.dataportal.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataportal.test.dto.DataBySubsetDto;
import visang.dataportal.test.dto.Test;
import visang.dataportal.test.dto.TestDto;

import java.util.List;

@Mapper
public interface TestMapper {

    List<TestDto> getTestList();
    TestDto getTestById(Long id);
    void createTest(Test test);

    List<DataBySubsetDto> getAllData();


}
