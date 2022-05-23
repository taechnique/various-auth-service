package io.teach.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.user.dto.model.UserAccountModel;

public class AuthRequestDto {

    @JsonProperty("user_account")
    private UserAccountModel userAccountModel;
}
