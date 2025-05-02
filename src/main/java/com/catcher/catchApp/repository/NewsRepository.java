package com.catcher.catchApp.repository;

import com.catcher.catchApp.entity.News;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsRepository extends MongoRepository<News, String> {
}