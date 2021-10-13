package jpa.jpa_shop.service;

import jpa.jpa_shop.domain.item.Book;
import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.member.Address;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.domain.orders.OrderStatus;
import jpa.jpa_shop.domain.repository.OrderRepository;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;
import jpa.jpa_shop.exception.NotEnoughStockException;
import jpa.jpa_shop.service.IFS.OrderServiceIFS;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderServiceIFS orderService;
    @Autowired
    OrderRepository orderRepository;

    @Rollback(value = true)
    @Test
    public void order() {
        // Given
        Member member=createMember();
        Item item=createBook();
        int orderCnt=3;
        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCnt);

        //then
        Order order = orderRepository.findById(orderId);
        assertThat(OrderStatus.ORDER).isEqualTo(order.getStatus());
        assertThat(1).isEqualTo(order.getOrderItems().size());
        assertThat(7).isEqualTo(item.getStockQuantity());
        assertThat(30000).isEqualTo(order.getTotalPrice());
    }
    @Test(expected = NotEnoughStockException.class)
    public void StockException() throws Exception{
        //Given
        Member member=createMember();
        Item item=createBook();
        int orderCnt=11;

        //When
        orderService.order(member.getId(),item.getId(),orderCnt);

        //Then
        fail("재고 수량 예외 발생.");
    }

    @Test
    public void cancelOrder() {
        //Given
        Member member=createMember();
        Item item=createBook();
        int orderCnt=2;

        //When
        Long orderId=orderService.order(member.getId(),item.getId(),orderCnt);
        orderService.cancelOrder(orderId);

        //Then
        Order cancelOrder = orderRepository.findById(orderId);
        assertThat(cancelOrder).isNotNull();
        assertThat(cancelOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).isEqualTo(10);
    }

    @Test
    public void SearchMemberNameAndOrderStatus() throws Exception{

        //given
        List<Member> member=createListMember();
        Member member1=member.get(0);
        Member member2=member.get(1);
        Member member3=member.get(2);
        Item item=createBook();
        int orderCnt=2;

        //when
        // 주문
        Long orderId=orderService.order(member1.getId(),item.getId(),orderCnt);
        Long orderId2=orderService.order(member1.getId(),item.getId(),orderCnt+2);
        Long orderId3=orderService.order(member2.getId(),item.getId(),orderCnt);
        Long orderId4=orderService.order(member3.getId(),item.getId(),orderCnt);
        // 주문 취소
        orderService.cancelOrder(orderId4);

        // 주문한 사람. 주문 상태 : 3건
        OrderSearchRequestDto requestDto = OrderSearchRequestDto.of(null, OrderStatus.ORDER);
        List<Order> orders = orderService.SearchMemberNameAndOrderStatus(requestDto);

        // 주문한 member1 이력 : 2건
        OrderSearchRequestDto requestDto2 = OrderSearchRequestDto.of(member1.getName(),null);
        List<Order> orders1 = orderService.SearchMemberNameAndOrderStatus(requestDto2);

        // 주문 취소한 사람 이력 : 1건
        OrderSearchRequestDto requestDto3 = OrderSearchRequestDto.of(null, OrderStatus.CANCEL);
        List<Order> orders2 = orderService.SearchMemberNameAndOrderStatus(requestDto3);

        //then
        Assertions.assertThat(orders.size()).isEqualTo(3);
        Assertions.assertThat(orders1.size()).isEqualTo(2);
        Assertions.assertThat(orders2.size()).isEqualTo(1);
        Assertions.assertThat(item.getStockQuantity()).isEqualTo(2); // 재고 현황 check
    }

    private Member createMember()
    {
        Member member = Member.builder().
                name("PARK").
                address(Address.builder().detail("Seoul").street("soso street").zipcode("59-1").build())
                .build();
        em.persist(member);
        return member;
    }
    private Book createBook()
    {
        Book book = Book.builder()
                .name("문제해결전략")
                .author("구종만")
                .isbn("111-xxxx-xxxxxx")
                .stockQuantity(10)
                .price(10000)
                .build();
        em.persist(book);
        return book;
    }


    private List<Member> createListMember()
    {
        List<Member> list=new LinkedList<>();
        Member member = Member.builder().
                name("PARK").
                address(Address.builder().detail("Seoul").street("soso street").zipcode("59-1").build())
                .build();
        list.add(member);
        em.persist(member);
        Member member2 = Member.builder().
                name("KIM").
                address(Address.builder().detail("NEYYORK").street("HELLO street").zipcode("59-1").build())
                .build();
        em.persist(member2);
        list.add(member2);
        Member member3 = Member.builder().
                name("LEE").
                address(Address.builder().detail("BUSAN").street("MAMA! street").zipcode("59-1").build())
                .build();
        em.persist(member3);
        list.add(member3);
        return list;
    }
}