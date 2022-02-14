package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity //해당 파일로 시큐리티를 활성화
@Configuration //IoC등록   
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oAuth2DetailsService;
	/* 로그인 실패 핸들러 의존성 주입 */
	private final AuthenticationFailureHandler customFailureHandler;

	
	//비밀번호 BCrypt암호화
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//해당메소드 오버라이딩했을때, 나오는 super삭제 : 기존 시큐리티가 가지고 있는 기능이 다 비활성화됨.
		http.csrf().disable(); //csrf토큰 비활성화: 정상적가입이든 비정상적 가입이든 상관안쓴다는거임
		http.authorizeRequests()
			.antMatchers("/","/user/**","/image/**","/subscribe/**", "/comment/**","/api/**").authenticated() //해당 주소들은 인증이 필요하다
			.anyRequest().permitAll() //그게 아닌 모든 요청은 허용하겠다.
				.and() //그리고
			.formLogin() //인증이 필요한 페이지 요청이오면 로그인폼이 나오는데
			.loginPage("/auth/signin") // /auth/signin으로 가게할게 : GET방식
			.loginProcessingUrl("/auth/signin") //POST 방식 -> 스프링 시큐리티가 로그인 프로세스 진행
			.failureHandler(customFailureHandler) // 실패 핸들러
			.defaultSuccessUrl("/") //로그인이 성공적이면 "/"로 가게해라
				.and()
			.oauth2Login() //form로그인도 하는데, oauth2로 로그인도 할꺼야
			.userInfoEndpoint() //oauth2로그인을 하면 최종응답을 회원정보로 바로 받을 수 있다 (코드->인증토큰 과정은 너가해라(생략))
			.userService(oAuth2DetailsService); //그 과정을 하는 서비스를 정하기
	
	}
}
