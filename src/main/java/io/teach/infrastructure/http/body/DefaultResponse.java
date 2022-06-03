package io.teach.infrastructure.http.body;

import lombok.*;

import javax.validation.constraints.NotEmpty;

import static io.teach.infrastructure.excepted.ServiceStatus.success;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultResponse implements StandardResponse{

    @NotEmpty
    private String result;

    @Override
    public String result() {
        return result;
    }

    public static DefaultResponse ok() {
        final DefaultResponse response = new DefaultResponse();
        response.result = success();

        return response;
    }
}
