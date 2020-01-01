package au.com.qantas.web;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.StopWatch;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NonFunctionalTest extends BaseTest {

    @Test
    public void verifyHttpCallsAreCached() throws Exception {
        stub("qantas");
        stub("hotels");
        stub("shopping");
        stub("airbnb");
        stub("frequent-flyers", "frequent_flyers");
        stub("careers");
        stub("airbnb-offers", "airbnb_offers");
        stub("david-jones", "david_jones");
        stub("the-good-guys", "good_guys");
        stub("search");

        mockMvc.perform(get("/web-resource")
                .param("url", "http://localhost:8080/qantas")
                .param("depth", "3")
                .param("page", "1")
                .param("size", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].nodes[0]").exists())
                .andExpect(jsonPath("$.data.nodes", hasSize(4)));

        verify(1, getRequestedFor(urlEqualTo("/careers")));
        verify(1, getRequestedFor(urlEqualTo("/search")));
    }

    @Test
    public void verifyNodesAreResolvedConcurrently() throws Exception {
        final Integer fixedDelayInMilliSecs = 400;
        stub("qantas");
        stub("hotels", fixedDelayInMilliSecs);
        stub("shopping", fixedDelayInMilliSecs);
        stub("frequent-flyers", "frequent_flyers", fixedDelayInMilliSecs);
        stub("careers", fixedDelayInMilliSecs);

        final StopWatch stopWatch = new StopWatch();


        final MockHttpServletRequestBuilder request = get("/web-resource")
                .param("url", "http://localhost:8080/qantas")
                .param("depth", "1")
                .param("page", "1")
                .param("size", "4");

        stopWatch.start();
        final ResultActions resultActions = mockMvc.perform(request);
        stopWatch.stop();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nodes", hasSize(4)));
        assertThat((double) stopWatch.getTotalTimeMillis(), closeTo(fixedDelayInMilliSecs, 200));
    }
}
