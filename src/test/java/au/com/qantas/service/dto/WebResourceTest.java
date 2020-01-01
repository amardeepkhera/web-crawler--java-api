package au.com.qantas.service.dto;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class WebResourceTest {

    private static final String FIXED_HOST = "http://qantas.com/";

    @Test
    public void shouldReturnEmptyLinksForEmptyEmptyLinks() {
        final WebResource webResource = WebResource.builder()
                .build();

        webResource.paginate(PageRequest.of(1, 1));

        assertThat(webResource.getLinks(), empty());
    }

    @Test
    public void shouldHaveFirstLinkForPageOneAndSizeOne() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(1, 1));

        assertThat(webResource.getLinks(), contains(generateLinks(1).toArray()));
    }


    @Test
    public void shouldHaveFirstFiveLinksForPageOneAndSizeFive() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(1, 5));

        assertThat(webResource.getLinks(), contains(generateLinks(5).toArray()));
    }

    @Test
    public void shouldHaveFirstNineLinksForPageOneAndSizeNine() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(1, 9));

        assertThat(webResource.getLinks(), contains(generateLinks(1, 9).toArray()));
    }

    @Test
    public void shouldHaveAllTenLinksForPageOneAndSizeTwelve() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(1, 12));

        assertThat(webResource.getLinks(), contains(generateLinks(10).toArray()));
    }

    @Test
    public void shouldHaveSecondLinkForPageTwoAndSizeOne() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(2, 1));

        assertThat(webResource.getLinks(), contains(generateLinks(2, 2).toArray()));
    }

    @Test
    public void shouldHaveLastFiveLinksForPageTwoAndSizeFive() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(2, 5));

        assertThat(webResource.getLinks(), contains(generateLinks(6, 10).toArray()));
    }

    @Test
    public void shouldHaveLastOneLinkForPageTwoAndSizeNine() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(2, 9));

        assertThat(webResource.getLinks(), contains(generateLinks(10, 10).toArray()));
    }

    @Test
    public void shouldHaveAllTenLinksForPageTwoAndSizeTwelve() {
        final WebResource webResource = WebResource.builder()
                .links(generateLinks(10))
                .build();

        webResource.paginate(PageRequest.of(2, 12));

        assertThat(webResource.getLinks(), contains(generateLinks(1, 10).toArray()));
    }


    private List<String> generateLinks(final int count) {
        return generateLinks(1, count);
    }

    private List<String> generateLinks(final int from, final int to) {
        return range(from, to + 1)
                .boxed()
                .map(n -> FIXED_HOST + n)
                .collect(toList());
    }

}