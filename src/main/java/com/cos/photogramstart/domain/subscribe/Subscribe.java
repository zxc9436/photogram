package com.cos.photogramstart.domain.subscribe;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( //2개이상의 컬럼을 복합적 unique를 걸기위해 사용
		uniqueConstraints = {
				@UniqueConstraint(
					name ="subscribe_uk", //제약조건 이름 : 보통 모델명_uk
					columnNames = {"fromUserId","toUserId"} //어떤것을 제약조건을 걸꺼냐
				)
		}
)
public class Subscribe {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@JoinColumn(name = "fromUserId") //언더스코어로 컬럼명이 만들어지기 싫어서 카멜케이스형식으로 만듦
	@ManyToOne
	private User fromUser; //구독하는유저
	
	@JoinColumn(name = "toUserId")
	@ManyToOne
	private User toUser; //구독받는유저
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 값이 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
