package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("apis/org")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {
}
