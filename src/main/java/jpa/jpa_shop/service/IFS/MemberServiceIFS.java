package jpa.jpa_shop.service.IFS;

import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberServiceIFS extends UserDetailsService {

    public void Join(MemberSaveRequestDto member);

    public void update(Long id, MemberUpdateRequestDto requestDto);

    public List<MemberResponseDto> findAll();

    public MemberResponseDto findById(Long MemberId);

    public MemberResponseDto findByUsername(String memberUsername);

    public void delete(Long id);
}
