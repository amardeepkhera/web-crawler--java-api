package au.com.qantas.web.controller;

import au.com.qantas.model.GetWebResourceResponse;
import au.com.qantas.model.WebResource;
import au.com.qantas.service.WebResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequiredArgsConstructor
public class WebResourceController implements IWebResourceController {


    private final WebResourceService webResourceService;

    @Override
    @GetMapping("/web-resource")
    public Resource<GetWebResourceResponse> getWebResource(final @RequestParam("url") URL url,
                                                           final @RequestParam(value = "depth", required = false, defaultValue = "1") Integer depth,
                                                           @PageableDefault(page = 1) final Pageable page) {
        final WebResource webResource = webResourceService.getWebResource(url, depth, page);

        final GetWebResourceResponse getWebResourceResponse = GetWebResourceResponse.builder()
                .data(webResource)
                .build();

        return newGetWebResourceResponseResource(getWebResourceResponse, depth, page);
    }

    private Resource<GetWebResourceResponse> newGetWebResourceResponseResource(final GetWebResourceResponse getWebResourceResponse,
                                                                               final Integer depth,
                                                                               final Pageable page) {
        final Resource<GetWebResourceResponse> responseResource = new Resource<>(getWebResourceResponse);
        responseResource.add(getWebResourceResponse.selfLink(depth, page));
        getWebResourceResponse.nextLink(depth, page).ifPresent(responseResource::add);
        getWebResourceResponse.previousLink(depth, page).ifPresent(responseResource::add);
        return responseResource;
    }
}
