package com.cos.photogramstart.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Integer>{

	@Query(value ="SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId =:principalId) ORDER BY id DESC", nativeQuery = true)
	Page<Image> mStory(int principalId, Pageable pageable);
	
	//좋아요가 있는 이미지만 출력
	@Query(value ="SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
	List<Image> mPopular();
 
	//태그검색
	@Query(value ="SELECT * FROM image WHERE tag LIKE :tag OR tag LIKE CONCAT('% ',:tag,' %') OR tag LIKE CONCAT('% ',:tag) OR tag LIKE CONCAT(:tag,' %') ORDER BY id DESC", nativeQuery = true)
	Page<Image> searchResult(@Param("tag") String tag, Pageable pageable);
}
