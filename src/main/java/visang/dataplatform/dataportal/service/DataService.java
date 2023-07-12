package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.dto.DataBySubjectDto;
import visang.dataplatform.dataportal.dto.TestDto;
import visang.dataplatform.dataportal.mapper.DataMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataMapper dataMapper;


    public List<TestDto> getTestList() {
        return dataMapper.getTestList();
    }

    public TestDto getTestById(Long id) {
        return dataMapper.getTestById(id);
    }

    public List<DataBySubjectDto> getAllData() {
        return dataMapper.getAllData();
    }



}
