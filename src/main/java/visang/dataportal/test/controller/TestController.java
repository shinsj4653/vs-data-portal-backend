package visang.dataportal.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataportal.test.dto.DataBySubjectDto;
import visang.dataportal.test.dto.Result;
import visang.dataportal.test.dto.TestDto;
import visang.dataportal.test.service.TestService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test/getTestList")
    public Result getTestList() {

        List<TestDto> result = testService.getTestList().stream()
                .collect(Collectors.toList());

        return new Result(result);

    }

    @GetMapping("/test/getTestById/{id}")
    public Result getTestById(@PathVariable Long id) {

        TestDto result = testService.getTestById(id);
        return new Result(result);

    }

    @GetMapping("/chart/getAllData")
    public Result getAllData() {
        List<DataBySubjectDto> result = testService.getAllData();
        return new Result(result);
    }





}
