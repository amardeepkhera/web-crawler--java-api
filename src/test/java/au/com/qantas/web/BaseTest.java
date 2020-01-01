package au.com.qantas.web;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public abstract class BaseTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule();

    @After
    public void cleanUp() {
        wireMockRule.resetAll();
    }

    @Autowired
    protected MockMvc mockMvc;

    protected void stub(final String url,
                        final String htmlFile,
                        final HttpStatus responseStatus,
                        final Integer fixedDelayInMilliSec) {
        stubFor(
                get(urlEqualTo("/" + url)).willReturn(
                        aResponse()
                                .withFixedDelay(fixedDelayInMilliSec)
                                .withStatus(responseStatus.value())
                                .withBodyFile(htmlFile + ".html")
                )
        );
    }

    protected void stub(final String url) {
        stub(url, url, HttpStatus.OK, 0);
    }

    protected void stub(final String url, final HttpStatus httpStatus) {
        stub(url, url, httpStatus, 0);
    }

    protected void stub(final String url, final Integer fixedDelayInMilliSec) {
        stub(url, url, HttpStatus.OK, fixedDelayInMilliSec);
    }

    protected void stub(final String url, final String htmlFile) {
        stub(url, htmlFile, HttpStatus.OK, 0);
    }

    protected void stub(final String url, final String htmlFile, final Integer fixedDelayInMilliSec) {
        stub(url, htmlFile, HttpStatus.OK, fixedDelayInMilliSec);
    }
}
