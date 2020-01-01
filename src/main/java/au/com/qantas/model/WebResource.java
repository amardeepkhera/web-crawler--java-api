package au.com.qantas.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

import java.net.URL;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Builder
@ToString
@Getter
@ApiModel(value = "Web Resource response")
public class WebResource {

    @ApiModelProperty(dataType = "string", value = "URL for the web resource.")
    private final URL url;

    @ApiModelProperty(value = "Title of the web resource.")
    private final String title;

    @ApiModelProperty(value = "Links present on the web resource.")
    @Default
    private List<WebResource> nodes = newArrayList();

    @ApiModelProperty(value = "Total links present on the web resource.")
    @Default
    private final Integer totalNodes = 0;

    @ApiModelProperty("URLs of the links presne on the web resource which could not be processed.")
    @Default
    private final List<String> unresolvedNodes = newArrayList();

}
