package io.teach.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teach.business.auth.controller.InfraController;
import io.teach.business.auth.controller.MemberJoinController;
import io.teach.infrastructure.config.RestDocsConfiguration;
import io.teach.infrastructure.http.body.TrackingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = {
        InfraController.class,
        MemberJoinController.class
})
@AutoConfigureRestDocs(outputDir = "target/generated-sources/snippets")
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public class DefaultRestDocsConfiguration {



    private MockMvc mockMvc;
    private MockMvc errorMockMvc;
    protected RestDocumentationResultHandler handler;
    protected RestDocumentationResultHandler errorHandler;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {

        this.handler = document("{class-name}/{method-name}"
                , preprocessRequest(prettyPrint(), modifyUris()
                        .scheme("https")
                        .host("www.yanolja.com")
                        .removePort())
                , preprocessResponse(prettyPrint()));

        this.errorHandler = document("{class-name}/{method-name}", preprocessResponse());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(this.handler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider).snippets().withEncoding("UTF-8"))
                .build();

        this.errorMockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(this.errorHandler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider).snippets().withEncoding("UTF-8")
                        .withDefaults(httpResponse()))
                .build();
    }

    private HttpHeaders getHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }

    protected ResultActions perform(final String uri, final HttpMethod method, Object dto) throws Exception {
        MockHttpServletRequestBuilder req = null;
        switch(method) {
            case GET:
                req = get(uri);
                break;
            case POST:
                req = post(uri);
                break;
            case PUT:
                req = put(uri);
                break;
            case DELETE:
                req = delete(uri);
                break;
            default: throw new IllegalArgumentException("Unsupported HTTP Method.");
        }

        final ResultActions result = this.mockMvc.perform(req
                .content(mapper.writeValueAsString(dto))
                .headers(getHeader()));

        return result;
    }

    protected FieldDescriptor field(String name, String description) {
        return field(name, description, false);
    }

    protected FieldDescriptor field(String name, String description, int length) {
        return field(name, description, length, false);
    }

    protected FieldDescriptor field(String name, String description, boolean optional) {
        return field(name, description, 0, optional);
    }

    protected FieldDescriptor field(String name, String description, int length, boolean optional) {

        FieldDescriptor descriptor = fieldWithPath(name)
                .description(description)
                .attributes(RestDocsAttributes.length(length));

        if(optional)
            descriptor.optional();

        return descriptor;
    }

    protected TrackingDto defaultTracking() {
        final TrackingDto tracking = new TrackingDto();

        tracking.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36");
        tracking.setUserIp("127.0.0.1");

        return tracking;
    }

    protected Snippet errorFields() {
        return responseFields(
                field("result", "처리 결과"),
                field("message", "결과 메세지")
        );
    }
}
