package Web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {//서로 연관된 필드들의 집합

    Member("ROLE_MEMBER","회원"),//권한1
    ADMIN("ROLE_ADMIN","관리자");///권한2

    private final String key;//고정변수
    private final String keyword;


}
