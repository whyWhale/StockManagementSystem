package jpa.jpa_shop.service;

import jpa.jpa_shop.domain.MiddleTable.OrderItem;
import jpa.jpa_shop.domain.delivery.Delivery;
import jpa.jpa_shop.domain.delivery.DeliveryStatus;
import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.repository.ItemRepository;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.repository.MemberRepository;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.domain.repository.OrderRepository;
import jpa.jpa_shop.exception.NoEntity;
import jpa.jpa_shop.service.IFS.OrderServiceIFS;
import jpa.jpa_shop.web.dto.request.order.OrderSaveRequestDto;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService implements OrderServiceIFS {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoEntity::new);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty())
            throw new NoEntity("No Item");
        Item item =optionalItem.get();

        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .status(DeliveryStatus.READY)
                .build();


        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    @Transactional
    public Long order(OrderSaveRequestDto orderSaveRequestDto) {
        Member member = memberRepository.findById(Long.parseLong(orderSaveRequestDto.getMemberId())).orElseThrow(NoEntity::new);
        Long[] dtoItems = orderSaveRequestDto.getItems();
        int[] count = orderSaveRequestDto.getCount();
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < dtoItems.length; i++) {
            Optional<Item> optionalItem = itemRepository.findById(dtoItems[i]);
            if(optionalItem.isEmpty())
                throw new NoEntity("No Item");
            Item item=optionalItem.get();
            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), count[i]));
        }

        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .status(DeliveryStatus.READY)
                .build();
        Order order = Order.createOrder(member, delivery, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.cancel();
    }

    @Override
    public List<Order> SearchMemberNameAndOrderStatus(OrderSearchRequestDto requestDto) {
        return orderRepository.findAll(requestDto);
    }

    @Override
    public List<Order> findWithMemberAndDelivery() {
        return orderRepository.findWithMemberAndDelivery();
    }

    @Override
    public List<Order> findWithMemberAndDelivery(int offset, int limit) {
        return orderRepository.findWithMemberAndDelivery(offset, limit);
    }

    @Override
    public List<Order> findAllWithItem() {
        return orderRepository.findAllWithItem();
    }


}
