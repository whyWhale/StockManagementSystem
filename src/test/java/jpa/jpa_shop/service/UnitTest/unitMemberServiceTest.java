package jpa.jpa_shop.service.UnitTest;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.member.Repository.MemberRepository;
import jpa.jpa_shop.exception.NoEntity;
import jpa.jpa_shop.service.MemberService;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class unitMemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberSaveRequestDto dto1;

    private MemberSaveRequestDto dto2;


    @Before
    public void memberData() {
        dto1 = MemberSaveRequestDto.builder()
                .name("KIM")
                .city("Seoul").street("soso street").zipcode("59-1")
                .build();
        ReflectionTestUtils.setField(dto1.toEntity(), "id", 1L);

        dto2 = MemberSaveRequestDto.builder()
                .name("PARK")
                .city("Incheon").street("gugu street").zipcode("12-1")
                .build();
        ReflectionTestUtils.setField(dto2.toEntity(), "id", 2L);

        log.info("before Test");
    }

    @Test
    public void MemberServiceJoin() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(dto1.toEntity()));

        // when
        memberService.Join(dto1);
        Member findMember = memberRepository.findById(1L).orElseThrow(NoEntity::new);

        // then
        assertThat(findMember.getName()).isEqualTo(dto1.getName());
        assertThat(findMember.getAddress().getCity()).isEqualTo(dto1.getCity());
        assertThat(findMember.getAddress().getStreet()).isEqualTo(dto1.getStreet());
        assertThat(findMember.getAddress().getZipcode()).isEqualTo(dto1.getZipcode());

    }

    @Test
    public void MemberServiceUpdate() {
        // given
        String name = "Park";
        String city = "Seoul";
        String street = "soso street";
        String zipcode = "65-1";
        MemberUpdateRequestDto Dto = MemberUpdateRequestDto.builder().name(name).
                city(city).street(street).zipcode(zipcode).build();

        given(memberRepository.findById(2L)).willReturn(Optional.of(dto2.toEntity()));

        // when

        memberService.update(2L, Dto);
        Optional<Member> optionalMember = memberRepository.findById(2L);
        final Member findMember = optionalMember.get();
        // then
        assertThat(findMember.getAddress().getCity()).isEqualTo(city);
        assertThat(findMember.getAddress().getStreet()).isEqualTo(street);
        assertThat(findMember.getAddress().getZipcode()).isEqualTo(zipcode);
        assertThat(findMember.getName()).isEqualTo(name);
    }

    @Test
    public void MemberServiceFindAll() {
        // given
        List<Member> list = getMembers();
        given(memberRepository.findAll()).willReturn(list);
        // when
        List<MemberResponseDto> memberResponseDtos = memberService.findAll();
        // then
        MemberResponseDto memberResponseDto1 = memberResponseDtos.get(0);
        MemberResponseDto memberResponseDto2 = memberResponseDtos.get(1);
        assertThat(memberResponseDto1.getName()).isEqualTo(dto1.getName());
        assertThat(memberResponseDto1.getStreet()).isEqualTo(dto1.getStreet());
        assertThat(memberResponseDto2.getStreet()).isEqualTo(dto2.getStreet());
        assertThat(memberResponseDto2.getStreet()).isEqualTo(dto2.getStreet());
    }

    @Test
    public void MemberServiceDelete() {
        // given

        // when
        given(memberRepository.findById(1L)).willReturn(Optional.of(dto1.toEntity()));
        doNothing().when(memberRepository).delete(any(Member.class));
        // then
        final Optional<Member> byId = memberRepository.findById(1L);
        log.info("findById Member : {}",byId);
        memberService.delete(1L);
    }

    private List<Member> getMembers() {
        List<Member> list = new LinkedList<>();
        list.add(dto1.toEntity());
        list.add(dto2.toEntity());
        return list;
    }
}
