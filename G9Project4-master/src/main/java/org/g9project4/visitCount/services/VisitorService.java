package org.g9project4.visitCount.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.visitCount.VisitorCount;
import org.g9project4.visitCount.repositories.VisitorCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class VisitorService {
    private final VisitorCountRepository visitorCountRepository;

    public void recordVisit(){
        LocalDate today = LocalDate.now();
        Optional<VisitorCount> optionalCount = visitorCountRepository.findByVisitDate(today);

        if(optionalCount.isPresent()){
            VisitorCount visitorCount = optionalCount.get();
            visitorCount.setVisitCount(visitorCount.getVisitCount() + 1);
            visitorCountRepository.save(visitorCount);
        } else {
            VisitorCount visitorCount = new VisitorCount();
            visitorCount.setVisitDate(today);
            visitorCount.setVisitCount(1);
            visitorCountRepository.save(visitorCount);
        }
    }
    public List<VisitorCount> getVisitorStatistics(){
        List<VisitorCount> allStats = visitorCountRepository.findAllByOrderByVisitDateAsc();


        return allStats.stream()
                .sorted(Comparator.comparing(VisitorCount::getVisitDate).reversed())
                .limit(5) //
                .collect(Collectors.toList());
    }
}
