package com.catcher.catchApp.repository;

import com.catcher.catchApp.entity.CallHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CallHistoryRepository extends MongoRepository<CallHistory, String> {
}
