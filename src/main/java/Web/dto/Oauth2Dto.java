package Web.dto;

import Web.domain.MemberEntity;
import Web.domain.Role;
import lombok.*;

import java.util.Map;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
public class Oauth2Dto {// oauth 회원 정보 담고 있는 dto


    private String mid;
    private String registrationid; //현재 로그인 진행중인 서비스 코드를 구분. 네이버 로그인인지, 카카오 로그인인지 구별
    private Map<String,Object> attributes;//회원정보
    private String nameAttributeKey;//회원정보가 저장된 객체 이름

    // 1. 클라언트 구분 메소드 [ 네이버 or 카카오 ]  p.186 -> 1
    //OAuth2User에서 반환하는 사용자 정보가 Map으로 되어있기 때문에 값을 꺼내주기 위해 필요한 메소드
    public static Oauth2Dto sns확인 (String registrationid,Map<String,Object> attributes,String nameAttributeKey){
        if(registrationid.equals("naver")){
            return 네이버변환(registrationid,attributes,nameAttributeKey);
        }
        else if(registrationid.equals("kakao")){

            return 카카오변환(registrationid,attributes,nameAttributeKey);
        }
        else{
            return null;
        }
    }


    // 2. 만약에 registrationId 가 네이버 이면
    public static Oauth2Dto 네이버변환 (String registrationid,Map<String,Object> attributes,String nameAttributeKey){
       Map<String,Object> response =(Map<String, Object>) attributes.get(nameAttributeKey);
       return Oauth2Dto.builder()
               .mid((String)response.get("email")).build();
    }

    // 3. 만약에 registrationId 가 카카오 이면
    public static Oauth2Dto 카카오변환 (String registrationid,Map<String,Object> attributes,String nameAttributeKey){
        Map<String,Object> kakao_account =(Map<String, Object>) attributes.get(nameAttributeKey);
        return Oauth2Dto.builder()
                .mid((String)kakao_account.get("email")).build();
    }

    // oauth 정보 -> entity 변경
    //user엔티티를 생성.
    public MemberEntity toentity() {
        return MemberEntity.builder()
                .mid( this.mid )
                .role(Role.Member)///가입할 때 기본 권한을 멤버로 줌
                .build();
    }

}
