package io.teach.business.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.business.user.dto.model.UserAccountModel;
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
        System.out.println("Called by AuthRequestDto Constructor.");
    }

    public void setUserAccountModel(UserAccountModel userAccountModel) {
        System.out.println("set userAcountModel \n{userAccountModel}");
        this.userAccountModel = userAccountModel;
    }

    @Override
    public String toString() {

        return "{\n\t\"user_account\": "+userAccountModel+"\n}";
    }

}
