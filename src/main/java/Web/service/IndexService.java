package Web.service;

import Web.domain.MemberEntity;
import Web.domain.MemberRepository;
import Web.dto.LoginDto;
import Web.dto.MemberDto;
import Web.dto.Oauth2Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;


@Service                                                     // OAuth2UserService<OAuth2UserRequest , OAuth2User> : Oauth2 회원
public class IndexService implements UserDetailsService , OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // UserDetailsService 인터페이스. 인터페이스는 DB에서 유저 정보를 가져오는 역할
    @Autowired
    private MemberRepository memberRepository;


    public void signup(MemberDto memberDto) {
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
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKey()));
        // 인증된 엔티티의 키를 리스트에 보관
        LoginDto loginDto = new LoginDto(memberEntity.getMid(), memberEntity.getMpassword(), authorities);//아이디,비밀번호,권한
        return loginDto;// 회원엔티티 , 인증된 리스트 부여
    }

    //  *  oauth2 서비스 제공 메소드
    // OAuth2UserRequest : 인증 결과를 호출 클래스
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 인증[로그인] 결과 정보 요청
        //DefaultOAuth2User 서비스를 통해 User 정보를 가져와야 하기 때문에 대리자 생성
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);//네이버 로그인인지 카카오로그인인지 서비스를 구분해주는 코드
        //현재 로그인 진행중인 서비스 코드를 구분하기 위한 코드. 네이버 로그인인지, 구글 로그인인지 구별
        String registrationid = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2UserService를 통해 가져온 데이터를 담을 클래스
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String nameAttributeKey =//객체이름찾기
                userRequest
                        .getClientRegistration()//
                        .getProviderDetails()//
                        .getUserInfoEndpoint()//객체가 저장된 곳
                        .getUserNameAttributeName();//객체이름
        System.out.println(nameAttributeKey);
        // oauth2 정보 -> Dto -> entitiy -> db저장
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationid, attributes, nameAttributeKey);
        //  이메일로 엔티티호출
        Optional<MemberEntity> optional = memberRepository.findBymid(oauth2Dto.getMid());
        // 만약에 엔티티가 없으면
        if (!optional.isPresent()) {
            memberRepository.save(oauth2Dto.toentity());
        } else {

        }

        // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
        // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수  ]
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("SNS")),//권한 재정의
                attributes, nameAttributeKey);
    }

    public String 인증결과호출() {//로그인할때마다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//유저의 인증 정보 꺼내는 방법
        Object principal = authentication.getPrincipal();// username으로 해당하는 유저를 DB에서 조회해올 수 있음
        String mid = null;
        if (principal != null) { //해당 유저가 db에 존재할 때
            if (principal instanceof UserDetails) { /// 일반 유저
                mid = ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {//소셜 유저
                Map<String, Object> attributes = ((OAuth2User) principal).getAttributes();  ///소셜유저의 회원정보찾기
                if (attributes.get("response") != null) {///네이버유저일경우
                    Map<String, Object> map = (Map<String, Object>) attributes.get("response");///유저이름설정값이 response면 네이버이다->properties에서 확인가능
                    mid = map.get("email").toString();
                } else if (attributes.get("kakao_account") != null) {//카톡유저일경우
                    Map<String, Object> map = (Map<String, Object>) attributes.get("kakao_account");
                    mid = map.get("email").toString();
                }
            } else {
            }
        }
        return mid;
    }
}