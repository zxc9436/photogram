package com.cos.photogramstart.handler.ex;

public class CustomApiException extends RuntimeException{

	//객체를 구분할 때!! - 우리한텐 중요한게 아님
	private static final long serialVersionUID = 1L;
	
	public CustomApiException(String message) {
		super(message);
	}

}
