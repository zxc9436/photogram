package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationException extends RuntimeException{

	//객체를 구분할 때!! - 우리한텐 중요한게 아님
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errorMap;
	
	public CustomValidationException(String message, Map<String,String> errorMap) {
		super(message); //부모 클래스에 getter가 있기에 굳이 message에 대한 getter를 만들지 않아도 된다.
		this.errorMap = errorMap;
	}
	
	public CustomValidationException(String message) {
		super(message); //부모 클래스에 getter가 있기에 굳이 message에 대한 getter를 만들지 않아도 된다.
	}
	
	public Map<String, String> getErrorMap(){
		return errorMap;
	}
}
