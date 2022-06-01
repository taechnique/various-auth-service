package io.teach.infrastructure.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

@Data
@Component
@ConfigurationProperties("verify")
public class VerifyProperties {

    private EmailPolicy emailPolicy;



    @Data
    public static class EmailPolicy {

        @Min(1)
        private Integer todayMax;

        private Integer expiredSecond;

        private Integer codeLength;
    }
}
