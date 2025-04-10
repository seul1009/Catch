package com.catcher.catchApp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequest {
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;


    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "(?=.*[a-z])(?=.*[0-9])[a-z0-9A-Za-z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자 영문 소문자, 숫자를 포함해야 합니다.")
    private String password;

}