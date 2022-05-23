package io.teach.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.teach.user.dto.model.UserAccountModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequestDto {

    @JsonProperty("user_account")
    private UserAccountModel userAccountModel;

    @Override
    public String toString() {
        return String.format("{\n\t\"user_account\": %s\n}", userAccountModel);
    }
}
