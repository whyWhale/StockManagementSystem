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

    @NotBlank(message = "우편 번호는 필수 입니다.")
    private String zipcode;

    @NotBlank(message = "거리는 필수 입니다.")
    private String street;

    @NotBlank(message = "상세 주소는 필수 입니다.")
    private String detail;



    @Builder
    public MemberSaveRequestDto(String username, String password, String name, String detail, String street, String zipcode) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.detail = detail;
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
                .zipcode(this.getZipcode())
                .street(this.getStreet())
                .detail(this.getDetail())
                .build();
    }
}
