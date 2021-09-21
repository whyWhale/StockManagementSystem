package jpa.jpa_shop.web.dto.response.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;

    private String username;

    private String name;

    private String city;

    private String street;

    private String zipcode;

    @Builder
    public MemberResponseDto(Long id,String username ,String name, String city, String street, String zipcode) {
        this.id = id;
        this.username=username;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
