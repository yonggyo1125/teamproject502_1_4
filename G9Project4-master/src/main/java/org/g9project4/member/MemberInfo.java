package org.g9project4.member;

import lombok.Builder;
import lombok.Data;
import org.g9project4.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
public class MemberInfo implements UserDetails, Serializable {
    private final long serialVersionUID = 1L;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private transient Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {

        return email;
    }

    @Override
    public boolean isAccountNonExpired() {//계정 유효기간
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {//계정 잠금(정지)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//비번 교체
        return true;
    }

    @Override
    public boolean isEnabled() {//계정 탈퇴 여부
        return true;
    }
}
