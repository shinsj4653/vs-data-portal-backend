package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataService {

    private final MetaDataMapper metaDataMapper;

    public List<String> getMainCategory(String serviceName, String mainCategoryName) {
        return metaDataMapper.getMainCategory(serviceName, mainCategoryName);
    }
}
