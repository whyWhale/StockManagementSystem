package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.item.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MovieRepository extends JpaRepository<Movie,Long> , QuerydslPredicateExecutor<Movie> {
}
