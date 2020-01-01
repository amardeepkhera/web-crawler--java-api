package au.com.qantas.model;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@ApiModel
@Builder
@Getter
public class ErrorResponse {

    private final HttpStatus status;

    private final String message;

    @Builder.Default
    private final OffsetDateTime timestamp = OffsetDateTime.now();

}
