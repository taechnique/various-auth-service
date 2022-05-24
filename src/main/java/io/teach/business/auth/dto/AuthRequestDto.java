package io.teach.business.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.business.auth.dto.model.UserAccountModel;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class AuthRequestDto {

    @Valid
    @NotNull
    @JsonProperty("user_account")
    private UserAccountModel userAccountModel;

    public AuthRequestDto() {
    }

    public void setUserAccountModel(UserAccountModel userAccountModel) {
        this.userAccountModel = userAccountModel;
    }

    @Override
    public String toString() {

        return "{\n\t\"user_account\": "+userAccountModel+"\n}";
    }

}
