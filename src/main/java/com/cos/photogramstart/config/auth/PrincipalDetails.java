package com.cos.photogramstart.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;


@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;

	private User user;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user,Map<String, Object> attributes) {
		this.user = user;
	}
	
	//권한을 가져오는 함수
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add(()->{ return user.getRole(); }); //람다식
		return collector;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	//----------- 이부분은 다 true로-----------------
	//니 계정이 만료가 되었니?
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//니 계정이 잠겼니?
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//니 비밀번호를 한번도 안바꾼거아니니?
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//니 계정이 활성화 되어있니?
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes; // {id:12313452, name:이승민, email:zxc9436@naver.com}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return (String)attributes.get("name"); //
	}

}
