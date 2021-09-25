package jpa.jpa_shop.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.jpa_shop.domain.delivery.QDelivery;
import jpa.jpa_shop.domain.member.QMember;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.domain.orders.OrderStatus;
import jpa.jpa_shop.domain.orders.QOrder;
import jpa.jpa_shop.web.dto.request.order.OrderSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order)
    {
        em.persist(order);
    }

    public Order findById(Long id)
    {
        return em.find(Order.class,id);
    }
    // JPQL
    public List<Order> findAllJPQL(OrderSearchRequestDto orderSearch)
    {
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
            .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
    // queryDsl
    public List<Order> findAll(OrderSearchRequestDto orderSearch)
    {

        QOrder order= QOrder.order;
        QMember member= QMember.member;
        QDelivery delivery= QDelivery.delivery;

        JPAQueryFactory query=new JPAQueryFactory(em);

        return query
                .select(order)
                .from(order)
                .join(order.member,member)
                .join(order.delivery,delivery)
                .where(statusEquals(orderSearch.getOrderStatus()),nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEquals(OrderStatus orderStatus)
    {
        if(orderStatus==null)
            return null;
        return QOrder.order.status.eq(orderStatus);
    }

    private BooleanExpression nameLike(String memberName)
    {
        if(!StringUtils.hasText(memberName))
        {
            return null;
        }
        return QMember.member.name.like(memberName);
    }


    public List<Order> findWithMemberAndDelivery()
    {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d",Order.class).getResultList();
    }

    public List<Order> findAllWithItem()
    {
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i"
                ,Order.class).getResultList();
    }

    public List<Order> findWithMemberAndDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d",Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
