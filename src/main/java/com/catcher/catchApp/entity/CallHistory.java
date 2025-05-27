package com.catcher.catchApp.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "callHistory")
public class CallHistory {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime date;

    private String phoneNumber;
    private int vishingPercent;

    private String userId;

    private List<Message> messages;


}
