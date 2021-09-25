package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.item.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, QuerydslPredicateExecutor<Book> {

}
