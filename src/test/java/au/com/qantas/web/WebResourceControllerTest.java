package au.com.qantas.web;

import org.junit.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebResourceControllerTest extends BaseTest {


    @Test
    public void verifyGetWebResourceResponseForDepthEqualToOne() throws Exception {
        stub("qantas");
        stub("hotels");
        stub("shopping");

        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "2")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.url", is("http://localhost:8080/qantas")))
                .andExpect(jsonPath("$.data.title", is("Qantas")))
                .andExpect(jsonPath("$.data.totalNodes", is(4)))
                .andExpect(jsonPath("$.data.nodes").exists())
                .andExpect(jsonPath("$.data.nodes").isArray())
                .andExpect(jsonPath("$.data.nodes", hasSize(2)))
                .andExpect(jsonPath("$.data.nodes[0].url", is("http://localhost:8080/hotels")))
                .andExpect(jsonPath("$.data.nodes[0].title", is("Hotels")))
                .andExpect(jsonPath("$.data.nodes[0].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[0].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[0].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[0].nodes", empty()))
                .andExpect(jsonPath("$.data.nodes[0].unresolvedNodes", empty()))
                .andExpect(jsonPath("$.data.nodes[1].url", is("http://localhost:8080/shopping")))
                .andExpect(jsonPath("$.data.nodes[1].title", is("Shopping")))
                .andExpect(jsonPath("$.data.nodes[1].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[1].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[1].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[1].nodes", empty()))
                .andExpect(jsonPath("$.data.nodes[1].unresolvedNodes", empty()))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/web-resource?url=http://localhost:8080/qantas&depth=1&page=1&size=2")))
                .andExpect(jsonPath("$._links.next.href", is("http://localhost/web-resource?url=http://localhost:8080/qantas&depth=1&page=2&size=2")))
                .andExpect(jsonPath("$._links.previous").doesNotExist());

    }

    @Test
    public void verifyGetWebResourceResponseForDepthEqualToTwo() throws Exception {
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

        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("depth", "2")
                        .param("page", "1")
                        .param("size", "2")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.url", is("http://localhost:8080/qantas")))
                .andExpect(jsonPath("$.data.title", is("Qantas")))
                .andExpect(jsonPath("$.data.totalNodes", is(4)))
                .andExpect(jsonPath("$.data.nodes").exists())
                .andExpect(jsonPath("$.data.nodes").isArray())
                .andExpect(jsonPath("$.data.nodes", hasSize(2)))
                .andExpect(jsonPath("$.data.nodes[0].url", is("http://localhost:8080/hotels")))
                .andExpect(jsonPath("$.data.nodes[0].title", is("Hotels")))
                .andExpect(jsonPath("$.data.nodes[0].totalNodes", is(2)))
                .andExpect(jsonPath("$.data.nodes[0].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[0].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[0].nodes", hasSize(2)))
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].url", is("http://localhost:8080/airbnb")))
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].title", is("Airbnb")))
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[0].nodes[0].nodes", empty()))
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].url", is("http://localhost:8080/careers")))
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].title", is("Careers")))
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[0].nodes[1].nodes", empty()))
                .andExpect(jsonPath("$.data.nodes[1].title", is("Shopping")))
                .andExpect(jsonPath("$.data.nodes[1].totalNodes", is(3)))
                .andExpect(jsonPath("$.data.nodes[1].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[1].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].url", is("http://localhost:8080/david-jones")))
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].title", is("David Jones")))
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[1].nodes[0].nodes", empty()))
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].url", is("http://localhost:8080/the-good-guys")))
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].title", is("The Good Guys")))
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].totalNodes", is(0)))
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].nodes").exists())
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].nodes").isArray())
                .andExpect(jsonPath("$.data.nodes[1].nodes[1].nodes", empty()))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/web-resource?url=http://localhost:8080/qantas&depth=2&page=1&size=2")))
                .andExpect(jsonPath("$._links.next.href", is("http://localhost/web-resource?url=http://localhost:8080/qantas&depth=2&page=2&size=2")))
                .andExpect(jsonPath("$._links.previous").doesNotExist());

    }

    @Test
    public void verifyNodeForAWebResourceIsOmittedIfItCannotBeResolvedSuccessfully() throws Exception {
        stub("qantas");
        stub("hotels");
        stub("shopping", NOT_FOUND);
        stub("frequent-flyers", "frequent_flyers");
        stub("careers", INTERNAL_SERVER_ERROR);

        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "4")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalNodes", is(4)))
                .andExpect(jsonPath("$.data.nodes", hasSize(2)))
                .andExpect(jsonPath("$.data.unresolvedNodes", hasSize(2)));
    }

    @Test
    public void verifyGetWebResourceResponseForInvalidURLSyntax() throws Exception {

        mockMvc.perform(
                get("/web-resource")
                        .param("url", "htt://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "2")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void verifyGetWebResourceResponseForInvalidDepth() throws Exception {

        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "2")
                        .param("depth", "aa")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void verifyGetWebResourceResponseForURLWhichDoesNotExists() throws Exception {

        stub("qantas", NOT_FOUND);
        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "40")
        ).andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetWebResourceResponseForServerError() throws Exception {

        stub("qantas", INTERNAL_SERVER_ERROR);
        mockMvc.perform(
                get("/web-resource")
                        .param("url", "http://localhost:8080/qantas")
                        .param("page", "1")
                        .param("size", "40")
        ).andExpect(status().isInternalServerError());
    }

}