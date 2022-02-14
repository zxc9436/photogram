package com.cos.photogramstart.domain.likes;

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

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( // 2개 복합적 unique를 걸기위해 사용
		uniqueConstraints = { 
				@UniqueConstraint(
						name = "likes_uk", 
						columnNames = { "imageId", "userId" } // 어떤것들에 대해 제약조건을 걸꺼냐
						) 
				}
		)
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;

	@JoinColumn(name = "imageId")
	@ManyToOne  //기본 fetch전략이 eager
	private Image image; //하나의 이미지는 n개의 좋아요를 얻을 수 있다

	@JsonIgnoreProperties({ "images" })
	@JoinColumn(name = "userId")
	@ManyToOne  //기본 fetch전략이 eager
	private User user; //한명의 유저는 좋아요를 n번 할 수 있다

	private LocalDateTime createDate;

	@PrePersist // DB에 값이 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
