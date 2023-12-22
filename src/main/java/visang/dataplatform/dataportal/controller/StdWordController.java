package visang.dataplatform.dataportal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.StdWordService;

import java.util.List;

@RestController
@RequestMapping("/std/word")
@RequiredArgsConstructor
public class StdWordController {

    private final StdWordService stdWordService;

    @GetMapping
    ResponseDto<List<SimpleStdWordDto>> getStdWords(@RequestParam("page") int pageNum) {
        final List<SimpleStdWordDto> stdWords = stdWordService.getStdWords(pageNum);
        return ResponseUtil.SUCCESS("표준단어목록 조회 성공", stdWords);
    }

    @GetMapping
    ResponseDto<StdWordDetailDto> getStdWord(@PathVariable("id") Long id) {
        final StdWordDetailDto stdWord = stdWordService.getStdWord(id);
        return ResponseUtil.SUCCESS("표준단어 세부조회 성공", stdWord);
    }

//    @PostMapping


}