package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor //final 필드를 DI할때 사용
@Slf4j
@Controller //1.IoC등록    2. 파일을 리턴하는 컨트롤러
public class AuthController {
	
	private final AuthService authService; //전역변수에 final이 걸려있으면 무조건 초기화를 해줘야됨
	
	@GetMapping("/auth/signin")
	public String signinForm(@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "exception", required = false) String exception,
			Model model) {
		model.addAttribute("error", error); 
		model.addAttribute("exception", exception);

		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	//회원가입버튼 -> /auth/signup -> /auth/signin
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { //key=value (x-www-form-urlencoded)
		User user = signupDto.toEntity();
		authService.회원가입(user);
		return "auth/signin";
	}
}
