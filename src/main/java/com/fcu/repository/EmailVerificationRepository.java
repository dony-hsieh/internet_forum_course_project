package com.fcu.repository;

import com.fcu.model.EmailVerification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
    @Query(
            value = "SELECT * FROM EmailVerification AS e WHERE NOW() > e.expired_time",
            nativeQuery = true
    )
    List<EmailVerification> findAllExpiredVerification();
}
