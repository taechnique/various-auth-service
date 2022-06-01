package io.teach.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.Charset;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class DefaultRestDocsConfiguration {

    @Autowired
    private MockMvc mockMvc;

    public void performGet(final String uri, final HttpMethod method) {
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
        mockMvc.perform(req).andDo(document())
    }
}
