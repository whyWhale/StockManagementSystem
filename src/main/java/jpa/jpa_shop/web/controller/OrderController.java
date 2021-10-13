package jpa.jpa_shop.web.controller;

import jpa.jpa_shop.Config.Security.SecurityMember;
import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.service.IFS.ItemServiceIFS;
import jpa.jpa_shop.service.IFS.MemberServiceIFS;
import jpa.jpa_shop.service.IFS.OrderServiceIFS;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
@Controller
public class OrderController {
    private final OrderServiceIFS orderService;
    private final MemberServiceIFS memberService;
    private final ItemServiceIFS itemService;

    @GetMapping("")
    public String createForm(Model model,Authentication authentication)
    {
        final Member member = ((SecurityMember) authentication.getPrincipal()).getMember();
        List<Item> itemList=itemService.findItemsToOrder();
        log.info("members -> {}",member);
        model.addAttribute("members",member);
        model.addAttribute("items",itemList);
        return "order/orderForm";
    }

    @GetMapping("/list")
    public String orderList(@ModelAttribute("orderSearch") OrderSearchRequestDto searchRequestDto, Model model)
    {
        log.info("searchRequestDto ===========>{}",searchRequestDto.toString());
        List<Order> orders = orderService.SearchMemberNameAndOrderStatus(searchRequestDto);
        model.addAttribute("orders",orders);
        return "order/orderList";
    }


    @PostMapping("")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId, @RequestParam("count") int count)
    {
        orderService.order(memberId,itemId,count);
        return "redirect:/order/list";
    }

    @PostMapping("/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId)
    {
        orderService.cancelOrder(orderId);
        return "redirect:/order/list";
    }
}
