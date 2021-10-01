package jpa.jpa_shop.web.controller;

import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.service.IFS.MemberServiceIFS;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.request.member.MemberSearchConditionDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final MemberServiceIFS memberService;

    @GetMapping("/members")
    public String list(Model model
            , @ModelAttribute("PageRequestDto") PageRequestDTO requestDTO
            , @ModelAttribute("MemberSearchCondition") MemberSearchConditionDto searchConditionDto)
    {
        log.info("PageRequestDTO : {}",requestDTO);
        log.info("MemberSearchCondition : {}",searchConditionDto);
        final PageResponseDTO<MemberResponseDto, Member> dto = memberService.pagingMembers(requestDTO, searchConditionDto);
        model.addAttribute("members",dto.getDtoList());
        model.addAttribute("result",dto);
        return "member/memberList";
    }
}
