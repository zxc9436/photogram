/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
 */

// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId,obj) {
	if ($(obj).text() === "구독취소") {
		//구독취소 성공
		$.ajax({
			type:"delete",
			url:"/api/subscribe/"+toUserId,
			dataType:"json"
		}).done(res=>{
			$(obj).text("구독하기");
			$(obj).toggleClass("blue"); //toggle이란 클래스에 css를 넣었다 뺐다를 자유롭게 할 수 있음
		}).fail(error=>{
			console.log("구독취소실패",error);
		});
		
		
	} else {
		//구독하기 성공
		$.ajax({
			type:"post",
			url:"/api/subscribe/"+toUserId,
			dataType:"json"
		}).done(res=>{
			$(obj).text("구독취소");
			$(obj).toggleClass("blue");
		}).fail(error=>{
			console.log("구독하기실패",error);
		});
	}
}

// (2) 구독자 정보 모달 보기
function subscribeInfoModalOpen(pageUserId) {
	$(".modal-subscribe").css("display", "flex");
	
	$.ajax({
		url:`/api/user/${pageUserId}/subscribe`,
		dataType:"json"
	}).done(res=>{
		console.log(res.data);
		res.data.forEach((u)=>{ //for문
			let item = getSubscribeModalItem(u);
			$("#subscribeModalList").append(item);
		})
	}).fail(error=>{
		console.log("구독정보 불러오기 오류",error);
	});
}

function getSubscribeModalItem(u) {
	let item = `<div class="subscribe__item" id="subscribeModalItem-${u.id}">
	<div class="subscribe__img">
		<img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/basic.png'"/>
	</div>
	<div class="subscribe__text">
		<h2><a href="/user/${u.id}" id="commentId">${u.username}</a></h2>
	</div>
	<div class="subscribe__btn">`;
	
	if(!u.equalUserState){ //동일한 유저가 아닐 때 버튼이 만들어져야됨
		if(u.subscribeState){ //구독한 상태
			item +=`<button class="cta blue" onclick="toggleSubscribe(${u.id},this)">구독취소</button>`
		}else{ //구독 안한 상태
			item +=`<button class="cta" onclick="toggleSubscribe(${u.id},this)">구독하기</button>`
		}
	}
		
	item += `
	</div>
</div>`;
	return item;
}


// (3) 유저 프로파일 사진 변경 (완)
function profileImageUpload(pageUserId, principalId) {
	
	if(pageUserId != principalId){ //값만 비교 타입도 비교할려면 !== 사용
		alert("프로필 사진을 수정할 수 없는 유저입니다");
		return;
	}
	

	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}
		
		//서버에 이미지를 전송
		let profileImageForm = $("#userProfileImageForm")[0];
		
		//FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있다.
		let formData = new FormData(profileImageForm); //값들만 담김
		
		$.ajax({
			type:"put",
			url:`/api/user/${principalId}/profileImageUrl`, //응답받을 url
			data:formData,
			contentType:false, //필수 : 디폴트 값인 x-www-form-unlencoded로 파싱되는 것을 방지
			processData:false, //필수 : contentType을 false로 줬을 때 QueryString 자동 설정됨. 그것도 해제
			enctype:"multipart/form-data",
			dataType:"json" //응답type
		}).done(res=>{
			// 사진 전송 성공시 이미지 변경
			let reader = new FileReader();
			reader.onload = (e) => {
			$("#userProfileImage").attr("src", e.target.result);
			}
			reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
		}).fail(error=>{
			console.log("오류",error);
		});
		
	});
}


// (4) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}


// (5) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

// (6) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

// (7) 구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}

// (8) 프로필 페이지 게시글 모달통신
function storyInfoModalOpen(imageId) {
	$(".modal-story").css("display", "flex");
	$.ajax({
		url:`/api/user/${imageId}`,
		dataType:"json"
	}).done(res=>{
		console.log("스토리 모달 출력성공",res.data);
			let item = getStoryModalItem(res.data);
			$("#modalStory").append(item);
	}).fail(error=>{
		console.log("스토리 모달 출력실패",error);
	});
}

function getStoryModalItem(i){
	let item = `
	<div class="story" id=story-${i.id}>
	
		<div class="story-header">
			<span>스토리 테스트
			`;
			if(principalId == i.user.id){
				item +=`
				<button type="button" id="modi" onclick="popup('.modal-delete',${i.id})" style="border: none; background: none;">
						<i class="fas fa-bars"></i>
				</button>`;
			}
			item +=`
			</span>	
				<button onclick="modalClose()">
					<i class="fas fa-times"></i>
				</button>
		</div>
		
		<div class="story-list" id="storyModalList">
			<div class="story-image" id="storyImage">
				<img src="/upload/${i.postImageUrl}" alt="error image" style="width:100%; height:646px;">
			</div>
			
			<div class="story-content" id="storyContent">
				<div class="storyTelling">
						<b>${i.user.username}&nbsp;&nbsp;</b>${i.caption}
						<div class="sl__item__contents__tags">`;
						
						i.tags.forEach((tags)=>{
									item +=`
										<span class="tag-spanModal" onclick="location.href='/image/search?name=${tags.name}'" >#${tags.name}</span>`;
									});
		   item +=`</div>
		   		<div id="storyCommentList-${i.id}">`;
		   		i.comment.forEach((comment)=>{
									item +=`<div id="storyCommentItem-${comment.id}">
													<p>
														<b><a href="/user/${comment.user.id}" id="commentId">${comment.user.username}</a> :</b> ${comment.content}
													</p>`;
					
									if(principalId == comment.user.id){
										item +=`<button onclick="deleteComment(${comment.id})">
														<i class="fas fa-times"></i>
													 </button>`;
									}
		   		
			item +=`</div>
				</div>
				<span class="commentDate" id="commentDate-${comment.id}"}>${comment.createDate}</span>`;
				});
				
				
			item +=`</div>
				<div class="storyLikes">
					<div class="sl__item__contents__icon">
					<button>`;
									//좋아요 아이콘
									if(i.likeState){
										item +=`<i class="fas fa-heart active" style="margin-left:15px;" id="storyLikeIcon-${i.id}" onclick="toggleLike(${i.id})"></i>`;
										
									}else{
										item +=`<i class="far fa-heart" style="margin-left:15px;" id="storyLikeIcon-${i.id}" onclick="toggleLike(${i.id})"></i>`;
										
									}
	  item +=`</button>
	  				</div>
	  			<span class="like"><b id="storyLikeCount-${i.id}">${i.likeCount} </b>likes</span>
	  				<p style="margin-left:15px; margin-top:10px;">${i.createDate}</p>
				</div>
				<div class="storyComment">
						<input type="text" placeholder="댓글 달기..."  id="storyCommentInput-${i.id}" style="width:92%; border:none; padding-left:20px; font-size:15px; padding-top:15px;"/>
						<button type="button" onclick="addComment(${i.id})" style="border:none; background-color:white; color:#01A9DB;"><b>게시</b></button>	
				</div>
			</div>
			<div class="modal-delete" onclick="modalDelete()">
				<div class="modal">
					<button onclick="myStoryDelete(deleteId)"><span style="color:red;"><strong>삭제</strong></span></button>
					<button onclick="closePopup('.modal-delete')">취소</button>
				</div>
			</div>
		</div>
	</div>
`;
	
	return item;
}

function modalDelete() {
	$(".modal-delete").css("display", "none");
}
function popup(obj, imageId) {
	deleteId = imageId;
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}

function myStoryDelete(imageId) {
		$.ajax({
		type:"delete",
		url:`/api/image/${imageId}`,
		dataType:"json"
	}).done(res=>{
		console.log("성공",res);
		$(`#story-${imageId}`).remove();
		window.location.replace("/");
	}).fail(error=>{
		console.log("실패",error);	
	})
}
/* js 파일 include하는 효과 */
//document.write('<script type="text/javascript" src="/js/story.js"></script>');
