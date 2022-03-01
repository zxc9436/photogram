	package com.cos.photogramstart.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.domain.likes.LikesRepository;
import com.cos.photogramstart.domain.tag.Tag;
import com.cos.photogramstart.domain.tag.TagRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.util.TagUtils;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final LikesRepository likesRepository;
	private final CommentRepository commentRepository;
	private final TagRepository tagRepository;

	
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(){
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true) //영속성 컨텍스트 변경감지를 해서, 더티체킹 flush(반영) X
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		//2(cos) 로그인
		// images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size()); //좋아요 카운트
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { //해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한것인지 비교
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	@Value("${file.path}") //application.yml의 file.path를 가져옴
	private String uploadFolder;
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto,PrincipalDetails principalDetails) {
		String fname = imageUploadDto.getFile().getOriginalFilename();
		String ext = fname.substring(fname.lastIndexOf(".")  + 1); //확장자만 추출
			if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif")) {
				UUID uuid = UUID.randomUUID(); //uuid -> 범용 유니크 식별자
				String imageFileName = uuid +"_"+imageUploadDto.getFile().getOriginalFilename(); //실제 파일네임이 들어감 ex) 1.jpg
				Path imageFilePath = Paths.get(uploadFolder+imageFileName); //실제 저장경로
				
				//try-catch 사용 이유 : 통신, I/O -> 예외가 발생할 수도 있다.
				try {
					Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); //실제 저장경로, 실제 이미지 파일(바이트화), null(생략가능)
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//image 테이블에 저장
				Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
				Image imageEntity = imageRepository.save(image);
				
				//Tag 저장
				if(imageUploadDto.getTags().length() == 0 || imageUploadDto.getTags().charAt(0) == '#') {
					List<Tag> tags = TagUtils.parsingToTagObject(imageUploadDto.getTags(), imageEntity);
					tagRepository.saveAll(tags);
				}else {
					throw new CustomException("태그를 입력할 땐 # 을 붙혀주세요!!");
				}
				
				
				
			} else {
				throw new CustomException("이미지 파일만 업로드 할 수 있습니다.");
			}	
	}
	

	@Transactional
	public void 사진삭제(int ImageId) {
		 Image image = imageRepository.findById(ImageId).orElseThrow(()->{
			 throw new CustomException("해당 사진이 존재하지 않습니다");
		 });
		 try {
			  //관련된 likes의 정보 먼저 삭제해 준다.
		        likesRepository.deleteLikesByImage(image);
		        
		        //관련된 tag의 정보 먼저 삭제해 준다.
		        tagRepository.deleteTagsByImage(image);
		        
		        //관련된 Comment의 정보 먼저 삭제해 준다.
		        commentRepository.deleteCommentsByImage(image);

		        //관련 파일 저장 위치에서 삭제해 준다.
		        File file = new File(uploadFolder + image.getPostImageUrl());
		        file.delete();
		        imageRepository.deleteById(ImageId);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}     
	}
	
	@Transactional(readOnly = true)
    public Page<Image> 태그검색(String tagname, int principalId, Pageable pageable) {
		
        Page<Image> imageList = imageRepository.searchResult(tagname, pageable);

        imageList.forEach((image) -> {
        	image.setLikeCount(image.getLikes().size());
        	
        	image.getLikes().forEach((likes) -> {
                if(likes.getUser().getId() == principalId) image.setLikeState(true);
            });
        });
        return imageList;
    }
	
	@Transactional(readOnly = true)
	public Image 모달이미지(int imageId, int principalId){
		Image imageEntity = imageRepository.findById(imageId).orElseThrow(()->{
				throw new CustomException("이미지를 찾을 수 없습니다.");
			});
		
		imageEntity.setLikeCount(imageEntity.getLikes().size());
		imageEntity.getLikes().forEach((like)->{
			if(like.getUser().getId() == principalId) { //해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한것인지 비교
				imageEntity.setLikeState(true);
			}
		});
		return imageEntity;
	}
}
