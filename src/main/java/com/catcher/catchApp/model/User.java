package com.catcher.catchApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String id = UUID.randomUUID().toString();

    private String username;

    private String password;

    private int phishingCount = 0;

    private LoginType loginType; // 일반, 카카오 구분

    private String kakaoId;

    private String nickname;

    private String email;

}
