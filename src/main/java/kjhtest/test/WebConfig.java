package kjhtest.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class WebConfig {

    /**
     * Spring Boot 3 (Jakarta 기반)에서 파일 업로드를 활성화하려면
     * 반드시 MultipartResolver Bean 을 등록해줘야 한다.
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}