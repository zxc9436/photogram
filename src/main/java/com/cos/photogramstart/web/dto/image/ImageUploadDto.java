package com.cos.photogramstart.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	private MultipartFile file;
	private String caption;
	private String tag;
	
	//ImageUploadDto에서 Image 객체로 값을 보내게해주는 toEntity 메서드
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.tag(tag)
				.user(user)
				.build();
	}
	public void tagUpdate(String tag) {
		this.tag = tag;
	}
}
