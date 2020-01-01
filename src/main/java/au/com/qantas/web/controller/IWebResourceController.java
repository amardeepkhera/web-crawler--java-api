package au.com.qantas.web.controller;

import au.com.qantas.model.ErrorResponse;
import au.com.qantas.model.GetWebResourceResponse;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;

import java.net.URL;

public interface IWebResourceController {
    @ApiImplicitParams
            (value = {
                    @ApiImplicitParam(name = "page", value = "Page number", defaultValue = "1", dataType = "int", paramType = "query"),
                    @ApiImplicitParam(name = "size", value = "Size of the Page", defaultValue = "10", dataType = "int", paramType = "query")
            })
    @ApiOperation(value = "This endpoint returns data for a given web resource.", notes =
            "The nodes present on a web resource are returned on a best effort basis," +
                    " and will be omitted from the response if cannot be processed successfully.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Web resource successfully retrieved"),
            @ApiResponse(code = 404, message = "Web resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Server error", response = ErrorResponse.class)
    })
    Resource<GetWebResourceResponse> getWebResource(@ApiParam(value = "URL of the web resource to crawl") URL url,
                                                    @ApiParam(value = "Depth of the crawl") Integer depth,
                                                    Pageable page);
}
