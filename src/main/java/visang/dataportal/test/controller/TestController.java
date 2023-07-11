package visang.dataportal.test.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataportal.test.dto.Test;
import visang.dataportal.test.dto.TestDto;
import visang.dataportal.test.response.TestResponse;
import visang.dataportal.test.service.TestService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/getTestList")
    public Result getTestList() {

        List<TestDto> result = testService.getTestList().stream()
                .collect(Collectors.toList());

        return new Result(result);

    }

    @GetMapping("/getTestById/{id}")
    public Result getTestById(@PathVariable Long id) {

        TestDto result = testService.getTestById(id);
        return new Result(result);

    }



    // 데이터 테이블의 갯수에 따라 loc 생성해주는 함수 필요

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


}
