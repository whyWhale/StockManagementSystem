package jpa.jpa_shop.domain.member;

import jpa.jpa_shop.domain.orders.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Builder
    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @OneToMany(mappedBy = "member")
    private List<Order> orders=new LinkedList<>();
}
