package visang.dataportal.test.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataportal.test.dto.Test;
import visang.dataportal.test.dto.TestDto;
import visang.dataportal.test.mapper.TestMapper;
import visang.dataportal.test.mapper.TestRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    @PersistenceContext
    private final EntityManager em;

    public List<TestDto> getTestList() {
        return testMapper.getTestList();
    }

    public TestDto getTestById(Long id) {
        return testMapper.getTestById(id);
    }

    /**
     * GET : find All Test
      * @return : List<Test>
     */
    public List<Test> findTestList() {

        return testRepository.findAll();
    }



}
