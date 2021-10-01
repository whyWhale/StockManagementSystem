package jpa.jpa_shop.domain.repository.RepositoryCustom.Imple;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.repository.RepositoryCustom.CustomMemberRepository;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static jpa.jpa_shop.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Member> searchByConditions(Pageable pageable, MemberSearchConditionDto memberSearchConditionDto){
        final List<Member> contents = queryFactory.selectFrom(member).where(
                usernameContains(memberSearchConditionDto.getName()),
                cityContains(memberSearchConditionDto.getCity())
        ).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        final JPAQuery<Member> countQuery = queryFactory.selectFrom(member).where(
                usernameContains(memberSearchConditionDto.getName()),
                cityContains(memberSearchConditionDto.getCity())
        );
        return PageableExecutionUtils.getPage(contents,pageable,countQuery::fetchCount);
    }


    private BooleanExpression usernameContains(String name){
        return StringUtils.hasText(name) ? member.name.contains(name) : null;
    }

    private BooleanExpression cityContains(String city){
        return StringUtils.hasText(city) ? member.address.city.contains(city) : null;
    }
}
