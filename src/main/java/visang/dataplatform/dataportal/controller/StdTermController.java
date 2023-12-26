package visang.dataplatform.dataportal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdTermDetailDto;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.StdTermService;

import java.util.List;

@RestController
@RequestMapping("/apis/std/term")
@RequiredArgsConstructor
public class StdTermController {

    private final StdTermService stdTermService;

    @GetMapping
    ResponseDto<List<SimpleStdTermDto>> getStdTerms(@RequestParam("page") int pageNum) {
        final List<SimpleStdTermDto> stdTerms = stdTermService.getStdTerms(pageNum);
        return ResponseUtil.SUCCESS("표준용어목록 조회 성공", stdTerms);
    }

    @GetMapping("{id}")
    ResponseDto<StdTermDetailDto> getStdTerm(@PathVariable("id") Long id) {
        final StdTermDetailDto stdTerm = stdTermService.getStdTerm(id);
        return ResponseUtil.SUCCESS("표준용어 세부조회 성공", stdTerm);
    }

    @PostMapping
    ResponseDto<StdTermDetailDto> createStdTerm(@RequestBody CreateStdTermDto createStdTermDto) {
        stdTermService.createStdTerm(createStdTermDto);
        final StdTermDetailDto stdTerm = stdTermService.getStdTerm(createStdTermDto.getTerm_idx());
        return ResponseUtil.SUCCESS("표준용어 추가 성공", stdTerm);
    }

    @PutMapping("{id}")
    ResponseDto<StdTermDetailDto> updateStdTerm(@PathVariable("id") Long id, @RequestBody UpdateStdTermDto updateStdTermDto) {
        updateStdTermDto.setTerm_idx(id);
        stdTermService.updateStdTerm(updateStdTermDto);
        final StdTermDetailDto stdTerm = stdTermService.getStdTerm(id);
        return ResponseUtil.SUCCESS("표준용어 수정 성공", stdTerm);
    }

    @DeleteMapping("{id}")
    ResponseDto<StdTermDetailDto> deleteStdTerm(@PathVariable("id") Long id) {
        stdTermService.deleteStdTerm(id);
        return ResponseUtil.SUCCESS("표준용어 삭제 성공", null);
    }
}