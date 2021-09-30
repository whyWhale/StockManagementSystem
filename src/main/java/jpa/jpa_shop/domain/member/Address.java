package jpa.jpa_shop.domain.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
public class Address implements Serializable {
    private String city;
    private String street;
    private String zipcode;

    @Builder
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Address update(String city, String street, String zipcode)
    {
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
        return this;
    }
}
