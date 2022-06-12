package io.teach.business.auth.constant;

import java.util.Arrays;
import java.util.Optional;

public enum HistoryGroup {
    JOIN("JOIN"),
    PC_SIGNUP("pcSignup"),
    ;

    private String group;

    HistoryGroup(final String group) {
        this.group = group;
    }

    public static Optional<HistoryGroup> groupOf(final String group) {
        return Arrays.stream(values()).filter(g -> g.getGroup().equals(group))
                .findFirst();
    }

    public String getGroup() {
        return this.group;
    }
}
