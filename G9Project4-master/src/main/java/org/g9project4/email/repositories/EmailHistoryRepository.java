package org.g9project4.email.repositories;


import org.g9project4.email.entities.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long>, QuerydslPredicateExecutor<EmailHistory> {

}