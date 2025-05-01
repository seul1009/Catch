package com.catcher.catchApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private String sender;
    private String content;

    @ManyToOne
    @JoinColumn(name = "call_history_id")
    private CallHistory callHistory;
}
