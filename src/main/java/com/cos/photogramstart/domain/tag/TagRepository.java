package com.cos.photogramstart.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.photogramstart.domain.image.Image;

public interface TagRepository extends JpaRepository<Tag, Integer>{

	void deleteTagsByImage(Image image);
}
