package jpa.jpa_shop.domain.member;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.jpa_shop.configuration.TestQueryDslConfiguration;
import jpa.jpa_shop.domain.repository.MemberRepository;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@Import(TestQueryDslConfiguration.class)
@Slf4j
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    private MemberRepository memberRepository;
    @Before
    @Test
    public void initData() {
        IntStream.rangeClosed(0,20).forEach(i -> {
            memberRepository.save(Member.builder().name(Integer.valueOf(i).toString()).address(Address.builder().detail(Integer.valueOf(i/5).toString()).build()).build());
        });

        log.info("init data = {}",memberRepository.findAll());
    }
    @Test
    public void searchConditionTest() {
        // given
        final MemberSearchConditionDto searchConditionDto = MemberSearchConditionDto.builder().city("0").build();
        final PageRequest pageRequest = PageRequest.of(1, 3);

        // when
        final Page<Member> members = memberRepository.searchByConditions(pageRequest, searchConditionDto);
        final List<Member> content = members.getContent();
        log.info("contents data : {}",content); // 3,4
        // then
        Assertions.assertThat(content.size()).isEqualTo(2);
        Assertions.assertThat(content.get(0).getAddress().getDetail()).isEqualTo("0");
    }
}
