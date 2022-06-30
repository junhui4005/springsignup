package Web.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails { // 로그인 세션에 넣을 Dto 생성
    private String mid;
    private String mpassword;
    private Set<GrantedAuthority> authorities;// 인증권한

    public LoginDto(String mid, String mpassword, Collection<? extends GrantedAuthority>authorityList) {
        this.mid = mid;
        this.mpassword = mpassword;
        this.authorities = Collections.unmodifiableSet( new LinkedHashSet<>( authorityList ));//부여받은 권한을 수정할 수 없는 집합으로 만듦
    }


    // 인증검색
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    // 패스워드 반환
    @Override
    public String getPassword() {
        return this.mpassword;
    }

    // 아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }
    // 계정 인증 만료 여부 확인
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 계정 잠겨 있는지 확인
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 계정 패스워드가 만료 여부 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 계정 사용 가능한 여부 확인
    @Override
    public boolean isEnabled() {
        return true;
    }
}
