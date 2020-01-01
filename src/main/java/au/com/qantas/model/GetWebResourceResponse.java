package au.com.qantas.model;

import au.com.qantas.web.controller.WebResourceController;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Builder
@Getter
@ApiModel(description = "Container for Web Resource")
public class GetWebResourceResponse {

    private static final WebResourceController WEB_RESOURCE_CONTROLLER = methodOn(WebResourceController.class);

    private final WebResource data;


    public Link selfLink(final Integer depth, final Pageable page) {

        final UriComponents uriComponents = newUriComponents(page, depth);

        return new Link(new UriTemplate(uriComponents.toUriString()), "self");
    }

    public Optional<Link> nextLink(final Integer depth, final Pageable page) {
        if (data.getTotalNodes() > page.getPageNumber() * page.getPageSize()) {

            final UriComponents uriComponents = newUriComponents(page, depth, page.getPageNumber() + 1, page.getPageSize());

            return Optional.of(new Link(new UriTemplate(uriComponents.toUriString()), "next"));
        } else {
            return empty();
        }
    }

    public Optional<Link> previousLink(final Integer depth, final Pageable page) {
        if (page.getPageNumber() > 1 && data.getTotalNodes() <= page.getPageNumber() * page.getPageSize()) {

            final UriComponents uriComponents = newUriComponents(page, depth, page.getPageNumber() - 1, page.getPageSize());

            return Optional.of(new Link(new UriTemplate(uriComponents.toUriString()), "previous"));
        } else {
            return empty();
        }
    }

    private UriComponents newUriComponents(final Pageable page,
                                           final Integer depth,
                                           final Integer pageNumber,
                                           final Integer pageSize) {

        return linkTo(WEB_RESOURCE_CONTROLLER.getWebResource(data.getUrl(), depth, page))
                .toUriComponentsBuilder()
                .replaceQueryParam("page", pageNumber)
                .replaceQueryParam("size", pageSize)
                .build();
    }

    private UriComponents newUriComponents(final Pageable page,
                                           final Integer depth) {
        return newUriComponents(page, depth, page.getPageNumber(), page.getPageSize());
    }
}
