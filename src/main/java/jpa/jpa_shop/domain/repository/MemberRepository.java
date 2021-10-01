package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.repository.RepositoryCustom.CustomMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> , CustomMemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByUsername(String username);

    boolean existsByName(String username);
}

