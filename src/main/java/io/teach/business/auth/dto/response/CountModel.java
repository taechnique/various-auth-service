package io.teach.business.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountModel {

    @Min(0)
    @NotNull
    private Integer remain;

    @JsonInclude(Include.NON_NULL)
    private Integer use;

    public static CountModel left(final Integer remain) {
        return left(remain, null);
    }

    public static CountModel left(final Integer remain, final Integer use) {
        final CountModel count = new CountModel();
        count.remain = remain;
        count.use = use;
        return count;
    }

}
