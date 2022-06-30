package Web.service;

import Web.domain.MemberEntity;
import Web.domain.MemberRepository;
import Web.domain.Role;
import Web.dto.LoginDto;
import Web.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService implements UserDetailsService {
                        // UserDetailsService 인터페이스. 인터페이스는 DB에서 유저 정보를 가져오는 역할
    @Autowired
    private MemberRepository memberRepository;

    public void signup(MemberDto memberDto){
        memberRepository.save(memberDto.toentity());
    }

    // 로그인 서비스 제공 메소드
    @Override// 인증(로그인) 관리 메소드
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        //  회원 아이디로 엔티티 찾기
        MemberEntity memberEntity = memberRepository.findBymid(mid).get();
        //  찾은 회원엔티티의 권한[키] 을 리스트에 담기
        //GrantedAuthority : 부여된 인증의 클래스
        //   List<GrantedAuthority> : 부여된 인증들을 모아두기
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority( memberEntity.getRole().getKey() ));
        // 인증된 엔티티의 키를 리스트에 보관
        LoginDto loginDto = new LoginDto(memberEntity.getMid(),memberEntity.getMpassword(),authorities);//아이디,비밀번호,권한
        return loginDto;// 회원엔티티 , 인증된 리스트 부여
    }
}
