package jpa.jpa_shop.domain.repository.RepositoryCustom;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {

    public Page<Member> searchByConditions(Pageable pageable, MemberSearchConditionDto memberSearchConditionDto);
}
