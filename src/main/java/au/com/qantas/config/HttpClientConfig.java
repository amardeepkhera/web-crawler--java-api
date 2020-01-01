package au.com.qantas.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .interceptors(requestResponseInterceptor())
                .setReadTimeout(500)
                .setConnectTimeout(500)
                .build();
    }

    private ClientHttpRequestInterceptor requestResponseInterceptor() {
        return (request, body, execution) -> {
            log.info("Outbound call " + request.getURI().toString());
            ClientHttpResponse clientHttpResponse = execution.execute(request, body);
            log.info("Inbound response status for " + request.getURI() + " " + clientHttpResponse.getStatusText());
            return clientHttpResponse;
        };
    }

}
