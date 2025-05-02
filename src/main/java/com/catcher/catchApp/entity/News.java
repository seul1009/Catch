package com.catcher.catchApp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "news")
public class News {
    @Id
    private String id;

    private String title;
    private String content;

    public News() {}

    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}