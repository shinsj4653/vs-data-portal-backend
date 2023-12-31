package visang.dataplatform.dataportal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainSearchDto;
import visang.dataplatform.dataportal.model.response.common.ResponseDto;
import visang.dataplatform.dataportal.model.response.common.ResponseUtil;
import visang.dataplatform.dataportal.service.StdDomainService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/apis/std/domain")
@RequiredArgsConstructor
public class StdDomainController {

    private final StdDomainService stdDomainService;

    @GetMapping
    ResponseDto<List<SimpleStdDomainDto>> getStdDomains(@RequestParam("page") int pageNum) {
        final List<SimpleStdDomainDto> stdDomains = stdDomainService.getStdDomains(pageNum);
        return ResponseUtil.SUCCESS("표준도메인목록 조회 성공", stdDomains);
    }

    @GetMapping("{id}")
    ResponseDto<StdDomainDetailDto> getStdDomain(@PathVariable("id") Integer id) {
        final StdDomainDetailDto stdDomain = stdDomainService.getStdDomain(id);
        return ResponseUtil.SUCCESS("표준도메인 세부조회 성공", stdDomain);
    }

    @GetMapping("search")
    ResponseDto<List<StdDomainSearchDto>> searchStdDomain(@RequestParam("page") int pageNum, @RequestParam("keyword") String keyword) throws IOException {
        final List<StdDomainSearchDto> stdDomains = stdDomainService.searchStdDomain(pageNum, keyword);
        return ResponseUtil.SUCCESS("표준도메인검색 성공", stdDomains);
    }

    @PostMapping
    ResponseDto<StdDomainDetailDto> createStdDomain(@RequestBody CreateStdDomainDto createStdDomainDto) {
        stdDomainService.createStdDomain(createStdDomainDto);
        final StdDomainDetailDto stdDomain = stdDomainService.getStdDomain(createStdDomainDto.getDomain_idx());
        return ResponseUtil.SUCCESS("표준도메인 추가 성공", stdDomain);
    }

    @PutMapping("{id}")
    ResponseDto<StdDomainDetailDto> updateStdDomain(@PathVariable("id") Integer id, @RequestBody UpdateStdDomainDto updateStdDomainDto) {
        updateStdDomainDto.setDomain_idx(id);
        stdDomainService.updateStdDomain(updateStdDomainDto);
        final StdDomainDetailDto stdDomain = stdDomainService.getStdDomain(id);
        return ResponseUtil.SUCCESS("표준도메인 수정 성공", stdDomain);
    }

    @DeleteMapping("{id}")
    ResponseDto<StdDomainDetailDto> deleteStdDomain(@PathVariable("id") Integer id) {
        stdDomainService.deleteStdDomain(id);
        return ResponseUtil.SUCCESS("표준도메인 삭제 성공", null);
    }
}