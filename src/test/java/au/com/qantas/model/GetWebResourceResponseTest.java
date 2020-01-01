package au.com.qantas.model;


import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;

import java.net.URL;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GetWebResourceResponseTest {

    private final static String URL = "http://www.qantas.com";

    @Test
    public void verifySelfLink() {
        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(newWebResource())
                .build();

        final Link selfLink = getWebResourceResponse.selfLink(1, PageRequest.of(1, 1));

        assertThat(selfLink.getRel(), is("self"));
        assertThat(selfLink.getHref(), is("/web-resource?url=" + URL + "&depth=1&page=1&size=1"));
    }

    @Test
    public void verifyNextLink() {
        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(newWebResource(5))
                .build();

        final Optional<Link> nextLink = getWebResourceResponse.nextLink(1, PageRequest.of(1, 3));

        assertThat(nextLink.isPresent(), is(true));
        assertThat(nextLink.get().getRel(), is("next"));
        assertThat(nextLink.get().getHref(), is("/web-resource?url=" + URL + "&depth=1&page=2&size=3"));
    }

    @Test
    public void verifyNextLinkIsNotPresent() {
        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(newWebResource(5))
                .build();

        final Optional<Link> nextLink = getWebResourceResponse.nextLink(1, PageRequest.of(1, 5));

        assertThat(nextLink.isPresent(), is(false));
    }

    @Test
    public void verifyPreviousLink() {
        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(newWebResource(5))
                .build();

        final Optional<Link> nextLink = getWebResourceResponse.previousLink(1, PageRequest.of(2, 3));

        assertThat(nextLink.isPresent(), is(true));
        assertThat(nextLink.get().getRel(), is("previous"));
        assertThat(nextLink.get().getHref(), is("/web-resource?url=" + URL + "&depth=1&page=1&size=3"));
    }

    @Test
    public void verifyPreviousLinkIsNotPresentForPageOne() {
        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(newWebResource(5))
                .build();

        final Optional<Link> nextLink = getWebResourceResponse.previousLink(1, PageRequest.of(1, 3));

        assertThat(nextLink.isPresent(), is(false));
    }

    private WebResource newWebResource() {
        return newWebResource(0);
    }

    @SneakyThrows
    private WebResource newWebResource(final int totalNodeCount) {
        return WebResource.builder()
                .url(new URL(URL))
                .totalNodes(totalNodeCount)
                .build();
    }

}