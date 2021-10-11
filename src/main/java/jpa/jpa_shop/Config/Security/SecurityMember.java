package jpa.jpa_shop.Config.Security;

import jpa.jpa_shop.domain.member.Member;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@ToString
public class SecurityMember  extends User {
    Member member;
    public SecurityMember(Member member) {
        super(member.getUsername(), member.getPassword(),AuthorityUtils.createAuthorityList(member.getRoleKey()));
        this.member=member;
    }
}
