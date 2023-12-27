package visang.dataplatform.dataportal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordSearchDto;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.StdWordService;

import java.util.List;

@RestController
@RequestMapping("/apis/std/word")
@RequiredArgsConstructor
public class StdWordController {

    private final StdWordService stdWordService;

    @GetMapping
    ResponseDto<List<SimpleStdWordDto>> getStdWords(@RequestParam("page") int pageNum) {
        final List<SimpleStdWordDto> stdWords = stdWordService.getStdWords(pageNum);
        return ResponseUtil.SUCCESS("표준단어목록 조회 성공", stdWords);
    }

    @GetMapping("{id}")
    ResponseDto<StdWordDetailDto> getStdWord(@PathVariable("id") Integer id) {
        final StdWordDetailDto stdWord = stdWordService.getStdWord(id);
        return ResponseUtil.SUCCESS("표준단어 세부조회 성공", stdWord);
    }

    @GetMapping("search")
    ResponseDto<List<StdWordSearchDto>> searchStdWord(@RequestParam("page") int pageNum, @RequestParam("keyword") String keyword) {
        final List<StdWordSearchDto> stdWords = stdWordService.searchStdWord(pageNum, keyword);
        return ResponseUtil.SUCCESS("표준단어검색 성공", stdWords);
    }

    @PostMapping
    ResponseDto<StdWordDetailDto> createStdWord(@RequestBody CreateStdWordDto createStdWordDto) {
        stdWordService.createStdWord(createStdWordDto);
        final StdWordDetailDto stdWord = stdWordService.getStdWord(createStdWordDto.getWord_idx());
        return ResponseUtil.SUCCESS("표준단어 추가 성공", stdWord);
    }

    @PutMapping("{id}")
    ResponseDto<StdWordDetailDto> updateStdWord(@PathVariable("id") Integer id, @RequestBody UpdateStdWordDto updateStdWordDto) {
        updateStdWordDto.setWord_idx(id);
        stdWordService.updateStdWord(updateStdWordDto);
        final StdWordDetailDto stdWord = stdWordService.getStdWord(id);
        return ResponseUtil.SUCCESS("표준단어 수정 성공", stdWord);
    }

    @DeleteMapping("{id}")
    ResponseDto<StdWordDetailDto> deleteStdWord(@PathVariable("id") Integer id) {
        stdWordService.deleteStdWord(id);
        return ResponseUtil.SUCCESS("표준단어 삭제 성공", null);
    }
}