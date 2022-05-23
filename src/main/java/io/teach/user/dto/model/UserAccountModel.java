package io.teach.user.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountModel {


    private String username;

    @JsonInclude(Include.NON_EMPTY)
    private String password;

    @Override
    public String toString() {
        return String.format("{\n\t\"username\": %s,\n\t\"password\": %s\n}", this.username, this.password);
    }
}
