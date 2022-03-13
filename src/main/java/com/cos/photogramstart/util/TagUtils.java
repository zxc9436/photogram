package com.cos.photogramstart.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

public class TagUtils {
	 
	public static void parsingToTagObject(String tag, ImageUploadDto imageUploadDto){
		String temp[] = tag.split("#"); // #여행 #바다
		String tagJoin  = String.join(" ", tag.split("#")); //다시 문자 합체시켜줌
		imageUploadDto.tagUpdate(tagJoin.trim());
	}
}
