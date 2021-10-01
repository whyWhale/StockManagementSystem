package jpa.jpa_shop.service.IFS;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberServiceIFS extends UserDetailsService {

    public void Join(MemberSaveRequestDto member);

    public void update(Long id, MemberUpdateRequestDto requestDto);

    public void delete(Long id);

    public List<MemberResponseDto> findAll();

    public PageResponseDTO<MemberResponseDto, Member> pagingMembers(PageRequestDTO requestDTO, MemberSearchConditionDto searchConditionDto);

    public MemberResponseDto findById(Long MemberId);

    public MemberResponseDto findByUsername(String memberUsername);

}
