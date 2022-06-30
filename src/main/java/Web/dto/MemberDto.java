package Web.dto;

import Web.domain.MemberEntity;
import Web.domain.Role;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {

    private int mno;
    private String mid;
    private String mpassword;

    public MemberEntity toentity(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();//암호화

        return MemberEntity.builder()
                .mid(this.mid)
                .mpassword(        encoder.encode( this.mpassword )     )///입력받은 비밀번호를 암호화한다.
                .role( Role.Member)///권한을 멤버로 받는다.
                .build();
    }
}
