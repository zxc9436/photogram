package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(DB) 할 수 있는 API를 제공)
//User모델 : User 도메인
@Builder
@AllArgsConstructor //전체 생성자
@NoArgsConstructor //빈 생성자
@Data
@Entity //DB에 테이블 생성
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id; //크게 서비스할껀아니라 long 대신 int를 써도됨
	
	@Column(length=100, unique = true) //oauth2 로그인을 위해 칼럼길이 늘리기
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	private String website; //웹 사이트
	private String bio; //자기소개
	
	@Column(nullable = false)
	private String email; //이메일
	
	private String phone; //전화번호
	private String gender; //성별
	
	private String profileImageUrl; //유저 사진
	private String role; //권한
	
	//나는 연관관계의 주인이 아니다. 그러므로 테이블에 칼럼을 만들지마.
	//User를 select할 때 해당 User id로 등록된 image들을 다 가져와
	//Lazy = User를 select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages()함수의 image들이 호출될 때 가져와!!
	//Eager = User를 select할 때 해당 User id로 등록된 image들을 전부 Join 해서 가져와!!
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) 
	@JsonIgnoreProperties({"user"}) //도메인 image안에있는 user는 파싱하지마 - 무한참조현상 해결
	private List<Image> images; //양방향 매핑
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 값이 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}
	
	
}
