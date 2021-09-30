package jpa.jpa_shop.web.dto.request.member;

import jpa.jpa_shop.domain.member.Address;
import jpa.jpa_shop.domain.member.Member;
import jpa.jpa_shop.domain.member.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MemberSaveRequestDto {

    @NotBlank(message = "ID는 필수 입니다.")
    @Email(message = "올바른 이메일을 입력하세요")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "도시는 필수 입니다.")
    private String city;

    @NotBlank(message = "거리는 필수 입니다.")
    private String street;

    @NotBlank(message = "지번은 필수 입니다.")
    private String zipcode;

    @Builder
    public MemberSaveRequestDto(String username, String password, String name, String city, String street, String zipcode) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password=passwordEncoder.encode(this.password);
    }

    public Member toEntity()
    {
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .address(getAddress())
                .role(Role.ADMIN)
                .build();
    }

    private Address getAddress() {
        return Address.builder()
                .city(this.getCity())
                .street(this.getStreet())
                .zipcode(this.getZipcode())
                .build();
    }
}
