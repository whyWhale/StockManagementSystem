package jpa.jpa_shop.web.dto.request.member;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberSearchConditionDto {
    private String name;
    private String city;

    @Builder
    public MemberSearchConditionDto(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
