package org.g9project4.main.repositories;

import org.g9project4.main.entities.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {
    Optional<VisitorCount> findByVisitDate(LocalDate visitDate);
    List<VisitorCount> findAllByOrderByVisitDateAsc();
}
