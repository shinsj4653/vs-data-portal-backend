package visang.dataplatform.dataportal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import visang.dataplatform.dataportal.utils.HTMLCharacterEscapes;

@Configuration
@RequiredArgsConstructor
public class XssConfig implements WebMvcConfigurer {

    //이미 기존에 등록된 ObjectMapper Bean이 있다면 JSON 요청/응답에서 사용하기 위해 의존성 주입을 받아 사용한다.
    private final ObjectMapper objectMapper;

    // XSS 공격에 대한 Filter 적용
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> xssRegistrationBean = new FilterRegistrationBean<>();
        xssRegistrationBean.setFilter(new XssEscapeServletFilter());
        xssRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        xssRegistrationBean.addUrlPatterns("/*");
        return xssRegistrationBean;
    }

    // lucy-xss-servlet-filter는 @RequestBody로 전달되는 JSON 요청은 처리해주지 않는다.
    // 따라서, MessageConverter로 처리해줘야한다.
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }


}
