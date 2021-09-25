package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.item.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AlbumRepository extends JpaRepository<Album,Long> , QuerydslPredicateExecutor<Album> {
}
