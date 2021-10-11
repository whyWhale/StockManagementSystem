package jpa.jpa_shop.domain.orders;

import jpa.jpa_shop.domain.BaseEntity;
import jpa.jpa_shop.domain.MiddleTable.OrderItem;
import jpa.jpa_shop.domain.delivery.Delivery;
import jpa.jpa_shop.domain.delivery.DeliveryStatus;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.web.dto.response.order.OrderResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Order Cancel

    //======= 관게 매핑 =========


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new LinkedList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @Builder
    private Order(LocalDateTime orderDate, OrderStatus status, Member member, Delivery delivery) {
        this.orderDate = orderDate;
        this.status = status;
        this.member = member;
        this.delivery = delivery;
    }

    private void addOrderItems(OrderItem orderItem)
    {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems)
    {
        Order order=Order.builder()
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .member(member)
                .delivery(delivery)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItems(orderItem);
        }
        return order;
    }

    public static Order createOrder(Member member, Delivery delivery, List<OrderItem> orderItems)
    {
        Order order=Order.builder()
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .member(member)
                .delivery(delivery)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItems(orderItem);
        }
        return order;
    }

    // Business Logic
    public void cancel()
    {
        if(delivery.getStatus()== DeliveryStatus.COMPLETE)
        {
            throw new IllegalStateException("배송이 완료된 상품은 취소가 불가능합니다.");
        }
        this.status=OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice()
    {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

    public String LocalDateTimeFormat()
    {
        return this.orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public OrderResponseDto toDto()
    {
        return OrderResponseDto.builder()
                .id(this.id)
                .itemRepresentation(orderItems.get(0).getItem().getName())
                .totalPrice(this.getTotalPrice())
                .orderStatus(this.status)
                .memberName(this.getMember().getName())
                .address(this.delivery.getAddress())
                .orderDate(this.orderDate)
                .build();
    }
}
