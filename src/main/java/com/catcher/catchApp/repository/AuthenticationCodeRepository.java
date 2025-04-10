package com.catcher.catchApp.repository;

import com.catcher.catchApp.model.AuthenticationCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthenticationCodeRepository extends MongoRepository<AuthenticationCode, String> {
    Optional<AuthenticationCode> findByEmail(String email);
}
