package com.cos.photogramstart.handler.ex;
public class CustomException extends RuntimeException{

	//객체를 구분할 때!! - 우리한텐 중요한게 아님
	private static final long serialVersionUID = 1L;
	
	public CustomException(String message) {
		super(message);
		
	}

}
