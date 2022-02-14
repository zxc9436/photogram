package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service //1.IoC등록   2. 트랜잭션 관리
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Transactional //Write(Insert, Update, Delete)
	public User 회원가입(User user) {
		if(userRepository.findByUsername(user.getUsername()) != null) throw new CustomValidationException("이미 존재하는 username입니다");
		if(userRepository.findUserByEmail(user.getEmail()) != null) throw new CustomValidationException("이미 존재하는 email입니다");
		//회원가입 진행
		String rawPassword = user.getPassword(); //일반 비밀번호
		String encPassword = bCryptPasswordEncoder.encode(rawPassword); //암호화된 비밀번호
		
		user.setPassword(encPassword);
		user.setRole("ROLE_USER"); //관리자 ROLE_ADMIN
		
		User userEntity = userRepository.save(user);
		return userEntity;
		
	}
}
