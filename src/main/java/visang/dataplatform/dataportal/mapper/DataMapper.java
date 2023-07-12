package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.dto.DataBySubjectDto;
import visang.dataplatform.dataportal.dto.Test;
import visang.dataplatform.dataportal.dto.TestDto;

import java.util.List;

@Mapper
public interface DataMapper {

    List<TestDto> getTestList();
    TestDto getTestById(Long id);
    void createTest(Test test);

    List<DataBySubjectDto> getAllData();


}
