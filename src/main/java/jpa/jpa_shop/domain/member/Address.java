package jpa.jpa_shop.domain.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
public class Address implements Serializable {
    private String zipcode;
    private String street;
    private String detail;

    @Builder
    public Address(String detail, String street, String zipcode) {
        this.zipcode = zipcode;
        this.street = street;
        this.detail = detail;
    }

    public Address update(String city, String street, String zipcode)
    {
        this.zipcode=zipcode;
        this.street=street;
        this.detail=city;
        return this;
    }
}
