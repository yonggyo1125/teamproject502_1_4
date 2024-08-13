package com.jmt.email.repositories;

import com.jmt.email.entities.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long>, QuerydslPredicateExecutor<EmailHistory> {

}
