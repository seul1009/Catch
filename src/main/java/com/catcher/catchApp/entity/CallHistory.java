package com.catcher.catchApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Document(collection = "callHistory")
public class CallHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String date;
    private String phoneNumber;
    private int vishingPercent;

    @OneToMany(mappedBy = "callHistory", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();


}
