package org.g9project4.publicData.tour.repositories;

import org.g9project4.publicData.tour.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {
    @Query("SELECT DISTINCT c.category1, c.name1 FROM Category c")
    List<Object[]> findDistinctName1();

    @Query("SELECT DISTINCT c.category2, c.name2 FROM Category c WHERE c.category1 = :category1")
    List<Object[]> findDistinctCategory2ByCategory1(@Param("category1") String category1);

    @Query("SELECT DISTINCT c.category3, c.name3 FROM Category c WHERE c.category2 = :category2")
    List<Object[]> findDistinctCategory3ByCategory2(@Param("category2") String category2);

    @Query("SELECT DISTINCT c.name1 FROM Category c WHERE c.category1=:category1")
    String findDistinctName1ByCategory1(@Param("category1") String category1);

    @Query("SELECT DISTINCT c.name2 FROM Category c WHERE c.category2=:category2")
    String findDistinctName2ByCategory2(@Param("category2") String category2);

    @Query("SELECT DISTINCT c.name3 FROM Category c WHERE c.category3=:category3")
    String findDistinctName3ByCategory3(@Param("category3") String category3);
}
