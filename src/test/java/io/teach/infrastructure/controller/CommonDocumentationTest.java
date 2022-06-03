package io.teach.infrastructure.controller;

import io.teach.infrastructure.excepted.ServiceStatus;
import io.teach.infrastructure.snippet.CommonTableSnippet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ErrorCodeController.class)
@AutoConfigureRestDocs
public class CommonDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("공통 에러코드 세팅")
    public void commonTables() throws Exception {
        this.mockMvc.perform(get("/api/v1/docs/errors")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(document("common",
                            commonTableFields("common-error-table", null,
                                    attributes(key("title").value("공통 예외")),
                                    enumConvertFieldDescriptor(ServiceStatus.values())
                            ))
                        );
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(ServiceStatus [] statuses) {
        return Arrays.stream(statuses)
                .map(status -> fieldWithPath(status.name()).description(status.getMessage()))
                .toArray(FieldDescriptor[]::new);
    }

    public static CommonTableSnippet commonTableFields(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
                                                       Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CommonTableSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }

}
