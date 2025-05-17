package com.catcher.catchApp.repository;

import com.catcher.catchApp.entity.CallHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CallHistoryRepository extends MongoRepository<CallHistory, String> {
    List<CallHistory> findAllByUserIdOrderByDateDesc(String userId);
}
