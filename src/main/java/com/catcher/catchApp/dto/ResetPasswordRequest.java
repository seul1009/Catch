package com.catcher.catchApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{6,16}$", message = "비밀번호는 6~16자 영문 소문자, 숫자를 포함해야 합니다.")
    private String newPassword;
}
