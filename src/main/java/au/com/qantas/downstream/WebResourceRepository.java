package au.com.qantas.downstream;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Repository
@RequiredArgsConstructor
public class WebResourceRepository {

    private final RestTemplate restTemplate;

    @Cacheable("webResource")
    public String get(final URL url) {
        return restTemplate.getForObject(url.toString(), String.class);
    }

}
