package jpa.jpa_shop.web.controller;

import jpa.jpa_shop.Config.Security.SecurityMember;
import jpa.jpa_shop.service.CategoryService;
import jpa.jpa_shop.web.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Slf4j
@Controller
public class Home {

    private final CategoryService categoryService;

    @LogExecutionTime
    @GetMapping("")
    public String main(Model model, Authentication authentication, HttpSession httpSession) {
        if (authentication == null) {
            return "redirect:/member/login";
        }
        log.info(" auth member : {} ", ((SecurityMember) authentication.getPrincipal()).getMember().getUsername());
        log.info("session TTL : {}",httpSession.getMaxInactiveInterval());
        httpSession.setMaxInactiveInterval(120);
        model.addAttribute("auth", (SecurityMember) authentication.getPrincipal());

        return "index";
    }
}
