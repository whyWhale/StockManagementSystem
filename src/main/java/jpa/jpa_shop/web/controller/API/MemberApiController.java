package jpa.jpa_shop.web.controller.API;

import jpa.jpa_shop.service.IFS.MemberServiceIFS;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.ListResponse;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
@RestController
public class MemberApiController {
    private final MemberServiceIFS memberService;

    @GetMapping("")
    public ListResponse<MemberResponseDto> read() {
        List<MemberResponseDto> members = memberService.findAll();
        return new ListResponse(members.size(), members);
    }

    @GetMapping("/{id}")
    public MemberResponseDto findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping("")
    public ResponseEntity save(@Valid @RequestBody MemberSaveRequestDto requestDto) {
        log.info("{}", "create memberName -- >" + requestDto.getName());
        memberService.Join(requestDto);
        return ResponseEntity.ok("persist success");
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @Valid @RequestBody MemberUpdateRequestDto requestDto) {
        log.info("{}", "update Id -- >" + id);
        memberService.update(id, requestDto);
        return ResponseEntity.ok("update success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        log.info("{}", "delete Id -- >" + id);
        memberService.delete(id);
        return ResponseEntity.ok("delete success");
    }
}
