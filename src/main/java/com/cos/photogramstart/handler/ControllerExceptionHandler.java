package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

//모든 exception을 낚아챌려면
@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

	//runtimeException이 발동하는 모든게 이 메서드가 가로챔
	@ExceptionHandler(CustomValidationException.class)
	//CMRespDto<?> : 전역적으로 사용할것이기에 제네릭을 <?>로 하면 어떠한 타입도 다 가능
	public String validationException(CustomValidationException e) {
		//CMRespDto, Script 비교
		//1. 클라이언트에게 응답할 때는 script 좋음
		//2. Ajax 통신 - CMRespDto
		//3. Android 통신 - CMRespDto
		if (e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}
		return Script.back(e.getErrorMap().toString());
	}
	
	@ExceptionHandler(CustomException.class)
	public String Exception(CustomException e) {
		return Script.back(e.getMessage());
	}
	
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.getErrorMap()),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null),HttpStatus.BAD_REQUEST);
	}
}
