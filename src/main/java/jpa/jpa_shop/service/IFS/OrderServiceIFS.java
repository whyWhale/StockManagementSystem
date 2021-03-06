package jpa.jpa_shop.service.IFS;

import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.web.dto.request.order.OrderSaveRequestDto;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;

import java.util.List;

public interface OrderServiceIFS {

    public Long order(Long memberId, Long itemId, int count);

    public Long order(OrderSaveRequestDto orderSaveRequestDto);

    public void cancelOrder(Long orderId);

    public List<Order> SearchMemberNameAndOrderStatus(OrderSearchRequestDto requestDto);

    public List<Order> findWithMemberAndDelivery();

    public List<Order> findWithMemberAndDelivery(int offset, int limit);

    public List<Order> findAllWithItem();
}
