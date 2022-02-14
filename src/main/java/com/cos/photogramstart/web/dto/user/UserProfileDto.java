package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileDto {
	private boolean pageOwnerState; //페이지의 주인인지 아닌지
	private int imageCount;
	private boolean subscribeState; //구독을 한 상태인지
	private int subscribeCount; //구독한 숫자
	private User user;
}
