package io.teach.business.user.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountModel {
<<<<<<< HEAD:src/main/java/io/teach/user/dto/model/UserAccountModel.java
    
=======

>>>>>>> 0cf88030fd5057b3e75505ecf4cb522dd6f1312b:src/main/java/io/teach/business/user/dto/model/UserAccountModel.java
    private String username;

    @JsonInclude(Include.NON_EMPTY)
    private String password;

    @Override
    public String toString() {
        return String.format("{\n\t\"username\": %s,\n\t\"password\": %s\n}", this.username, this.password);
    }
}
