package jpa.jpa_shop.service.UnitTest;

import jpa.jpa_shop.domain.MiddleTable.OrderItem;
import jpa.jpa_shop.domain.delivery.Delivery;
import jpa.jpa_shop.domain.delivery.DeliveryStatus;
import jpa.jpa_shop.domain.item.Album;
import jpa.jpa_shop.domain.item.Book;
import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.repository.ItemRepository;
import jpa.jpa_shop.domain.member.Address;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.repository.MemberRepository;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.domain.orders.OrderStatus;
import jpa.jpa_shop.domain.repository.OrderRepository;
import jpa.jpa_shop.service.OrderService;
import jpa.jpa_shop.web.dto.request.order.OrderSaveRequestDto;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;
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
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class unitOrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    MemberRepository memberRepository;

    private Member member;
    private Item item;
    private Item item2;
    private Order order;
    private Order order2;
    private final int orderCnt =3 ;

    @Before
    public void Data() {
        member = Member.builder().
                name("KIM").
                address(Address.builder().detail("Seoul").street("soso street").zipcode("59-1").build())
                .build(); // id= 1L;
        ReflectionTestUtils.setField(member,"id",1L);

        item = Book.builder()

                .author("구종만")
                .isbn("isbn")
                .name("알고리즘 문제 해결 전략")
                .stockQuantity(10)
                .price(33000)
                .build();
        ReflectionTestUtils.setField(item,"id",1L);
        item2 = Album.builder()
                .artist("IU")
                .name("밤 편지")
                .price(55000)
                .stockQuantity(10)
                .build();
        ReflectionTestUtils.setField(item2,"id",2L);


        Delivery delivery = Delivery.builder().status(DeliveryStatus.READY).address(member.getAddress()).build();
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderCnt);
        order = Order.createOrder(member,delivery,orderItem);
        ReflectionTestUtils.setField(order,"id",1L);


        List<OrderItem> orderItems=new LinkedList<>();
        orderItems.add(OrderItem.createOrderItem(item,item.getPrice(),1));
        orderItems.add(OrderItem.createOrderItem(item2,item2.getPrice(),2));
        order2=Order.createOrder(member,delivery,orderItems);
        ReflectionTestUtils.setField(order2,"id",2L);


    }
    @Test
    public void OrderServiceCreateOrder() {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(orderRepository.findById(order.getId())).willReturn(order);
        // when
        orderService.order(member.getId(), item.getId(), 3);
        Order findOrder = orderRepository.findById(order.getId());
        // then
        assertThat(findOrder.getStatus()).isEqualTo(this.order.getStatus());
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.getMember()).isEqualTo(member);
        assertThat(findOrder.getTotalPrice()).isEqualTo(item.getPrice()*3);
    }

    @Test
    public void OrderServiceCreatesOrders() {
        // given
        OrderSaveRequestDto dto = OrderSaveRequestDto.builder().memberId("1").items(new Long[]{1L, 2L}).count(new int[]{1, 2}).build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(orderRepository.findById(order2.getId())).willReturn(order2);
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item2));

        // when
         orderService.order(dto);
        Order findOrder = orderRepository.findById(order2.getId());
        int totalPrice = findOrder.getTotalPrice();

        // then
        assertThat(findOrder.getStatus()).isEqualTo(this.order.getStatus());
        assertThat(findOrder.getOrderItems().size()).isEqualTo(2);
        assertThat(findOrder.getMember()).isEqualTo(member);
        assertThat(findOrder.getTotalPrice()).isEqualTo(item.getPrice()+item2.getPrice()*2);
    }

    @Test
    public void OrderServiceCancelOrder() {
        // given
        given(orderRepository.findById(order.getId())).willReturn(order);
        // when
        orderService.cancelOrder(order.getId());
        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(item.getStockQuantity()).isEqualTo(9);
    }

    @Test
    public void OrderServiceSearch() {
        // given
        OrderSearchRequestDto SearchDto
                = OrderSearchRequestDto.builder().memberName(null).orderStatus(OrderStatus.ORDER).build();
        OrderSearchRequestDto SearchCancelDto
                = OrderSearchRequestDto.builder().memberName(null).orderStatus(OrderStatus.CANCEL).build();
        List<Order> orders=new LinkedList<>();
        List<Order> cancelOrders=new LinkedList<>();
        order.cancel();
        orders.add(order2);
        cancelOrders.add(order);
        given(orderRepository.findAll(SearchDto)).willReturn(orders);
        given(orderRepository.findAll(SearchCancelDto)).willReturn(cancelOrders);

        // when
        List<Order> SearchOrders = orderService.SearchMemberNameAndOrderStatus(SearchDto);
        List<Order> SearchCancelOrders = orderService.SearchMemberNameAndOrderStatus(SearchCancelDto);

        // then
        assertThat(orders.size()).isEqualTo(SearchOrders.size());
        assertThat(cancelOrders.size()).isEqualTo(SearchCancelOrders.size());
    }
}
