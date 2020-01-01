package au.com.qantas.service;

import au.com.qantas.downstream.WebResourceRepository;
import au.com.qantas.service.dto.WebResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static au.com.qantas.service.parser.WebResourceParser.parse;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.subtract;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebResourceService {
    private final WebResourceRepository webResourceRepository;

    public au.com.qantas.model.WebResource getWebResource(final URL url,
                                                          final Integer depth,
                                                          final Pageable page) {
        final String body = webResourceRepository.get(url);

        final WebResource webResource = parse().apply(body, url).paginate(page);

        return recurse(webResource, depth, page);
    }

    private au.com.qantas.model.WebResource recurse(final WebResource webResource,
                                                    final Integer depth,
                                                    final Pageable page) {
        if (depth == 0) {
            return au.com.qantas.model.WebResource.builder()
                    .url(webResource.getUrl())
                    .title(webResource.getTitle())
                    .nodes(emptyList())
                    .build();
        } else {
            final List<au.com.qantas.model.WebResource> nodes = resolveLinksConcurrently(webResource.getLinks(), page)
                    .stream()
                    .map(node -> recurse(node, depth - 1, page))
                    .collect(toList());

            final List<String> unResolvedNodes = (List<String>) subtract(webResource.getLinks()
                    , nodes.stream()
                            .map(au.com.qantas.model.WebResource::getUrl)
                            .map(URL::toString)
                            .collect(toList())
            );
            return au.com.qantas.model.WebResource.builder()
                    .url(webResource.getUrl())
                    .title(webResource.getTitle())
                    .totalNodes(webResource.getTotalLinks())
                    .nodes(nodes)
                    .unresolvedNodes(unResolvedNodes)
                    .build();
        }
    }

    private List<WebResource> resolveLinksConcurrently(final List<String> links,
                                                       final Pageable page) {
        return links.stream()
                .map(this::toURL)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .parallel()
                .map(url -> resolveLink(url, page))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<WebResource> resolveLink(final URL link,
                                              final Pageable page) {
        return of(link)
                .flatMap(this::fetchLink)
                .map(body -> parse().apply(body, link).paginate(page));
    }

    private Optional<String> fetchLink(final URL link) {
        try {
            return Optional.ofNullable(webResourceRepository.get(link));
        } catch (Exception e) {
            log.warn("Could not fetch " + link, e);
            return empty();
        }
    }

    private Optional<URL> toURL(final String link) {
        return of(link)
                .flatMap(s -> {
                    try {
                        return of(new URL(link));
                    } catch (MalformedURLException e) {
                        log.warn("Error converting " + link + "to url", e);
                        return empty();
                    }
                });
    }
}
