package io.teach.business.auth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountModel {

    private Integer remain;

    public static CountModel left(final Integer remain) {
        final CountModel count = new CountModel();
        count.remain = remain;
        return count;
    }
}
