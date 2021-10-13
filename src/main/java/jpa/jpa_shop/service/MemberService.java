package jpa.jpa_shop.service;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.repository.MemberRepository;
import jpa.jpa_shop.Config.Security.SecurityMember;
import jpa.jpa_shop.exception.NoEntity;
import jpa.jpa_shop.service.IFS.MemberServiceIFS;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService implements MemberServiceIFS {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void Join(MemberSaveRequestDto requestDto) {
        requestDto.encodePassword(passwordEncoder);
        final Member member = requestDto.toEntity();
        if (validDuplicateMember(member)) {
            throw new IllegalArgumentException("Duplicated username");
        }
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void update(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(NoEntity::new);
        member.update(requestDto);
    }

    // private 은 Tranactional 안걸림.
    public boolean validDuplicateMember(Member member) {
        return memberRepository.existsByName(member.getName());
    }

    @Override
    public PageResponseDTO<MemberResponseDto,Member> pagingMembers(PageRequestDTO requestDTO, MemberSearchConditionDto searchConditionDto) {
        final Pageable pageable = requestDTO.getPageable(Sort.by("id"));
        final Page<Member> memberPages = memberRepository.searchByConditions(pageable, searchConditionDto);
        Function<Member, MemberResponseDto> function = Member::toDto;
        return new PageResponseDTO<>(memberPages,function);
    }


    @Override
    public MemberResponseDto findById(Long MemberId) {
        return memberRepository.findById(MemberId).orElseThrow(NoEntity::new).toDto();
    }

    @Override
    public MemberResponseDto findByUsername(String memberUsername) {
        return memberRepository.findByUsername(memberUsername).orElseThrow(NoEntity::new).toDto();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Member deleteMember = memberRepository.findById(id).orElseThrow(NoEntity::new);
        memberRepository.delete(deleteMember);
    }

    // security login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        log.info("member 조회 성공: {}", member.toString());
        SecurityMember securityMember = new SecurityMember(member);
        log.info("security member : {}", securityMember);
        return securityMember;
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream().map(Member::toDto).collect(Collectors.toList());
    }
}
