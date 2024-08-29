package org.g9project4.publicData.tour.repositories;

import org.g9project4.publicData.tour.entities.TourPlacePoint;
import org.g9project4.publicData.tour.entities.TourPlacePointId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TourPlacePointRepository extends JpaRepository<TourPlacePoint, TourPlacePointId>, QuerydslPredicateExecutor<TourPlacePoint> {

}
