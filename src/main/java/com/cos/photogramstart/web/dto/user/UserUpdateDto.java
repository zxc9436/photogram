package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {

	@NotBlank
	private String name; //필수
	@NotBlank
	private String password; //필수
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	
	//조금 위험함. 코드수정이 필요할 예정
	public User toEntity() {
		return User.builder()
				.name(name)
				.password(password) //만약 사용자 패스워드를 기재 안했으면 문제!! -> validation 체크!! 
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
