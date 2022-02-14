package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component //RestController, Service 모든 것들이 Component 상속해서 만듦 
@Aspect //공통기능 AOP를 처리할 수 있는 핸들러로 됨
public class ValidationAdvice {
	//@After :  특정 함수 실행후에 실행 
	//@Before :특정 함수전에 실행
	//@Around : @After,@Before 둘다 적용
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") //언제 등장할 것인지에대한 주소
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		//proceedingJoinPoint => 적용된 함수의 모든 곳에 접근할 수 있는 변수
		//적용된 함수보다 먼저 실행
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) { //arg(파리미터)안에 bindingResult가 있으면
	
				BindingResult bindingResult = (BindingResult) arg; //다운캐스팅
				
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMap = new HashMap<>();
					
					//에러담는공간
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성검사 실패함", errorMap);
				}
				
			}
		}
		return proceedingJoinPoint.proceed(); //원래 실행할려던 함수로 돌아가라(실행시켜라)
	}
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))") //언제 등장할 것인지에대한 주소
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) { //arg(파리미터)안에 bindingResult가 있으면
				
				BindingResult bindingResult = (BindingResult) arg; //다운캐스팅
				
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMap = new HashMap<>();
					
					//에러담는공간
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사 실패함", errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed();
	}
}
