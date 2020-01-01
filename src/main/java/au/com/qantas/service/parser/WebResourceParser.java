package au.com.qantas.service.parser;

import au.com.qantas.service.dto.WebResource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class WebResourceParser {

    private WebResourceParser() {
    }

    public static BiFunction<String, URL, WebResource> parse() {
        return (body, url) -> {
            final String sanitizedBody = Jsoup.clean(body, Whitelist.relaxed().addTags("title"));

            final Document document = Jsoup.parse(sanitizedBody);

            final List<String> links = links(document);

            return WebResource.builder()
                    .url(url)
                    .title(document.title())
                    .links(links)
                    .totalLinks(links.size())
                    .build();
        };
    }

    private static List<String> links(final Document document) {
        return document.select("a")
                .stream()
                .map(e -> e.attr("abs:href"))
                .filter(StringUtils::isNotEmpty)
                .filter(link -> link.startsWith("http"))
                .distinct()
                .collect(toList());
    }
}



