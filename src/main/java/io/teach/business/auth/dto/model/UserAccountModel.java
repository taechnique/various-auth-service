package io.teach.business.auth.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountModel {

    private String username;

    @JsonInclude(Include.NON_EMPTY)
    private String password;


    @Override
    public String toString() {
        return String.format("{\n\t\"username\": %s,\n\t\"password\": %s\n\t}", this.username, this.password);
    }
}
