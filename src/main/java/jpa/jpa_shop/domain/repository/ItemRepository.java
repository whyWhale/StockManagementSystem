package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item> {

}
