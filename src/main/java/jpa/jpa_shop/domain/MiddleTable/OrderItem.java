package jpa.jpa_shop.domain.MiddleTable;

import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.web.dto.response.orderItem.OrderItemResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    private int orderPrice;
    private int count;

    @Builder
    public OrderItem(int orderPrice, int count) {
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count)
    {
        OrderItem orderItem= OrderItem.builder()
                .orderPrice(orderPrice)
                .count(count)
                .build();
        orderItem.setItem(item);
        item.removeStock(count);
        return orderItem;
    }

    // businessLogic
    public void cancel() {
        getItem().addStock(count);
    }

    public int getTotalPrice()
    {
        return getOrderPrice()*getCount();
    }

    public OrderItemResponseDto toDto()
    {
        return OrderItemResponseDto.builder()
                .itemName(getItem().getName())
                .orderPrice(getOrderPrice())
                .count(count)
                .build();
    }
}
