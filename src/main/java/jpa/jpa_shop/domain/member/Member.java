package jpa.jpa_shop.domain.member;

import jpa.jpa_shop.domain.BaseEntity;
import jpa.jpa_shop.domain.orders.Order;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import jpa.jpa_shop.web.dto.response.member.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@DynamicUpdate
@ToString(exclude = {"password","role"})
@Entity
public class Member extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @Builder
    public Member(String username, String password, String name, Address address,Role role) {
        this.username = username;
        this.password = password;
        this.role=role;
        this.name = name;
        this.address=address;
    }

    public Member update(MemberUpdateRequestDto requestDto) {
        this.address.update(requestDto.getCity(), requestDto.getStreet(), requestDto.getZipcode());
        this.name= requestDto.getName();
        return this;
    }

    public MemberResponseDto toDto()
    {
        return MemberResponseDto.builder()
                .id(getId())
                .username(getUsername())
                .name(getName())
                .city(getAddress().getCity())
                .street(getAddress().getStreet())
                .zipcode(getAddress().getZipcode())
                .build();
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
