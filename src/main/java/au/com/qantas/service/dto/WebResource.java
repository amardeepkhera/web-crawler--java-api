package au.com.qantas.service.dto;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

import java.net.URL;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@Builder
@ToString
@Getter
public class WebResource {

    private final URL url;

    private final String title;

    @Default
    private List<String> links = newArrayList();

    @Default
    private Integer totalLinks = 0;

    @Default
    private List<String> unresolvedLinks = newArrayList();

    public WebResource paginate(final Pageable page) {
        if (links.size() < page.getPageSize()) {
            return this;
        }
        links = links
                .subList((page.getPageNumber() - 1) * page.getPageSize(), links.size())
                .stream()
                .limit(page.getPageSize())
                .collect(toList());
        return this;
    }
}
