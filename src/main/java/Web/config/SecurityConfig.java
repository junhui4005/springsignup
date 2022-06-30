package Web.config;

import Web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 해당 클래스 설정 // 암호화 제공 [ 특정 필드 암호화 ]
public class SecurityConfig extends WebSecurityConfigurerAdapter {
                                        // 웹 시큐리티 설정 관련 상속클래스
    @Override // 재정의
    protected void configure(HttpSecurity http) throws Exception {
                            // HTTP(URL) 관련 시큐리티 보안
        http
                .authorizeHttpRequests()// 인증된요청
                .antMatchers("/") // 해당 인증 권한 있을경우
                .permitAll() //  인증이 없어도 요청 가능   = 모든 접근 허용
                .and()
                .formLogin()// 로그인페이지 보안 설정 //보안 검증은 formLogin방식으로 하겠다.
                .loginPage("/member/login")// 아이디 / 비밀번호를 입력받을 페이지 URL
                .loginProcessingUrl("/member/logincontroller")   // 로그인 처리할 URL
                .failureUrl("/member/login/error")//로그인 실패시 이동할 URL
                .usernameParameter("mid") // 로그인시 아이디로 입력받을 변수명
                .passwordParameter("mpassword")// 로그인시 비밀번호로 입력받을 변수명
                .and()
                .logout()//로그아웃 처리
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))// 로그아웃 처리 URL
                .logoutSuccessUrl("/")//로그아웃 성공 후 이동페이지
                .invalidateHttpSession(true)// 로그아웃 이후 세션 전체 삭제 여부
                .and()
                .csrf() // csrf : 사이트 간 요청 위조 = 서버에게 요청할수 있는 페이지 제한
                .ignoringAntMatchers("/member/logincontroller")//csrf예외처리
                .ignoringAntMatchers("/member/signupcontoller");//csrf예외처리
//        super.configure(http);
    }

    @Autowired
    private IndexService  indexService;///서비스 호출


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//AuthenticationManagerBuilder: 유저 인증정보를 설정 할 수 있다.
        auth.userDetailsService(indexService).passwordEncoder(new BCryptPasswordEncoder());
            // 인증할 서비스객체                    패스워드 인코딩(   BCrypt 객체로  )
    }
}
