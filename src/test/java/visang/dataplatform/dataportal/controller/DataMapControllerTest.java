package visang.dataplatform.dataportal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import visang.dataplatform.dataportal.service.DataMapService;

@ExtendWith(MockitoExtension.class)
public class DataMapControllerTest {

    @Mock
    private DataMapService dataMapService;

    @InjectMocks
    private DataMapController dataMapController;

    private MockMvc mockMvc;

    @DisplayName("데이터 맵 대분류 정보 반환 API 테스트")
    @Test
    public void 데이터맵_대분류_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/category/main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 대분류 단위까지의 데이터 조회에 성공하였습니다."));

    }

    @DisplayName("데이터 맵 중분류 정보 반환 API 테스트")
    @Test
    public void 데이터맵_중분류_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/category/sub"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 중분류 단위까지의 데이터 조회에 성공하였습니다."));

    }

    @DisplayName("데이터 맵 주요 데이터셋 반환 API 테스트")
    @Test
    public void 데이터맵_주요_데이터셋_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/dataset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 주요 데이터 셋 조회에 성공하였습니다."));

    }
}