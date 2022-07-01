package Web.domain;

import lombok.*;

import javax.persistence.*;

@Entity@Builder
@Setter@Getter@AllArgsConstructor@NoArgsConstructor@ToString
public class MemberEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int mno;
    private String mid;
    private String mpassword;
    @Enumerated( EnumType.STRING )
    private Role role;//권한

}
