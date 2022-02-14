package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; //Repository는 EntityManager를 구현해서 만들어져 있는 구현체
												  //직접 쿼리를 작성하기위해 선언함
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int pricipalId, int pageUserId){
		
		//쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, "); //쿼리작성할 때 끝에 한칸 무조건 띄어야됨
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append("if((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); //맨끝에는 세미클론 첨부하면 안됨
		
		// 1.물음표		pricipalId
		// 2.물음표		pricipalId
		// 3.물음표 	    pageUserId
		
		//쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, pricipalId)
				.setParameter(2, pricipalId)
				.setParameter(3, pageUserId);
		
		//쿼리 실행
		JpaResultMapper result = new JpaResultMapper(); //qlrm 라이브러리 사용 -> 데이터베이스에서 result된 결과를 java 클래스에 매핑해주는 라이브러리
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class); //1건만 리턴받을꺼면 uniqueResult()사용, 지금은 여러건 리턴하기에 list()사용
		
		return subscribeDtos;
	}
	
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다");
		}
		
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
