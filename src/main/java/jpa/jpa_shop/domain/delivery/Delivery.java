package jpa.jpa_shop.domain.delivery;

import jpa.jpa_shop.domain.member.Address;
import jpa.jpa_shop.domain.orders.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Delivery {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Builder
    public Delivery(Address address, DeliveryStatus status) {
        this.address = address;
        this.status = status;
    }
}
