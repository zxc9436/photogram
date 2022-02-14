package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscribeDto {
	
	private int id; //누구를 구독할지
	private String username;
	private String profileImageUrl;
	private Integer subscribeState; //mariadb는 int라고하면 db true값을 리턴을 못받기에 integer로 함
	private Integer equalUserState; //지금 화면에 뜬 사용자가 동일인이지 아닌지

}
