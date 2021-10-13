package jpa.jpa_shop.service;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Rollback(value = false)
    public void join() {
        //given
        String username = "abc1234@naver.com";
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
                .username(username)
                .password("password")
                .name("KIM")
                .detail("Seoul").street("soso street").zipcode("59-1")
                .build();
        requestDto.encodePassword(passwordEncoder);
        //when
        memberService.Join(requestDto);
        //then
        final MemberResponseDto byUsername = memberService.findByUsername(username);
        log.info("findByUsername : {}",byUsername.toString());
        assertThat(byUsername.getUsername()).isEqualTo(requestDto.getUsername());
    }

    @Test
    public void findAll() {
        MemberSaveRequestDto dto1 = MemberSaveRequestDto.builder()
                .name("PARK")
                .password("password")
                .detail("Seoul").street("soso street").zipcode("59-1")
                .build();

        MemberSaveRequestDto dto2 = MemberSaveRequestDto.builder()
                .name("LEE")
                .detail("Seoul").street("gogo street").zipcode("11-1")
                .build();
        memberService.Join(dto1);
        memberService.Join(dto2);

        List<MemberResponseDto> all = memberService.findAll();
        assertThat(all).isNotNull();
        assertThat(all.get(0).getUsername()).isEqualTo(dto1.getUsername());
        assertThat(all.get(1).getUsername()).isEqualTo(dto2.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void validDuplicateMember() throws Exception {
        //given
        MemberSaveRequestDto dto1 = MemberSaveRequestDto.builder()
                .username("PARK")
                .detail("Seoul").street("soso street").zipcode("59-1")
                .build();

        MemberSaveRequestDto dto2 = MemberSaveRequestDto.builder()
                .username("PARK")
                .detail("Seoul").street("gogo street").zipcode("11-1")
                .build();
        //when
        memberService.Join(dto1);
        memberService.Join(dto2);
        //then
        fail();
    }

    @Test
    public void pagingMembers() {
        // given
        IntStream.rangeClosed(0,19).forEach(i -> {
            final MemberSaveRequestDto saveRequestDto = MemberSaveRequestDto.builder()
                    .name(Integer.valueOf(i).toString())
                    .password(Integer.valueOf(i).toString())
                    .detail(Integer.valueOf(i / 5).toString())
                    .build();

            saveRequestDto.encodePassword(passwordEncoder);
            memberService.Join(saveRequestDto);
        });

        final MemberSearchConditionDto searchConditionDto = MemberSearchConditionDto.builder().city("0").build();
        final PageRequestDTO pageRequest=PageRequestDTO.builder().page(2).size(3).build();

        log.info("init data = {}",memberService.findAll());
        // when
        final PageResponseDTO<MemberResponseDto, Member> pageResponseDTO
                = memberService.pagingMembers(pageRequest, searchConditionDto);
        final List<MemberResponseDto> content = pageResponseDTO.getDtoList();
        log.info("contents data : {}",content); // 3,4
        // then
        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).getCity()).isEqualTo("0");
    }
}