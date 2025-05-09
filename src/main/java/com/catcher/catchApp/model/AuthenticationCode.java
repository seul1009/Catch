package com.catcher.catchApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "authcode")
public class AuthenticationCode {
    @Id
    private String id;
    private String email;
    private String code;

    private Date createdAt = new Date();

    public AuthenticationCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.createdAt = new Date();
    }
}

