package au.com.qantas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class DocumentationConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.com.qantas.web"))
                .paths(regex("/web-resource.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Web Crawler API",
                "Web Crawler API Documentation.",
                "V1",
                null,
                new Contact("Amardeep Khera", null, "amardeepsinghkhera@gmail.com"),
                null, null, emptyList());
    }
}
