/**
	2. 검색 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */


// (0) 현재 로그인한 사용자 아이디
let principalId = $("#principalId").val();
let tag = document.getElementById("tag").value;



// (1) 스토리 로드하기
let page = 0;


function searchLoad() {
	
	$.ajax({
		url:`/api/image/search?tag=${tag}&page=${page}`,
		dataType:"json"
	}).done(res=>{
		if(res.data.totalElements == 0){
			alert("검색 결과가 없습니다.");
			window.history.back();
		}
		console.log("성공", res);
		res.data.content.forEach((image)=>{
			let storyItem = getSearchItem(image);
			$("#storyList").append(storyItem);
		})
	}).fail(error=>{
		console.log("오류",error);
	});
}
searchLoad();

function getSearchItem(image) {
	let item =`<div class="story-list__item"  id="storyListItem-${image.id}">
						<div class="sl__item__header">
							<div>
								<a href="/user/${image.user.id}"><img class="profile-image" src="/upload/${image.user.profileImageUrl}"
									onerror="this.src='/images/basic.png'" /></a>
							</div>
							<div><b><a href="/user/${image.user.id}">${image.user.username}</a></b></div>`;
							if(principalId == image.user.id){
								item +=`
								<div style="float:right;">
									<button type="button" class="modi" onclick="popup('.modal-info',${image.id})" style="border: none; background: none;">
										<i class="fas fa-bars"></i>
									</button>
								</div>`;
								}
	
						item +=`
						</div>
							

						<div class="sl__item__img">
							<img src="/upload/${image.postImageUrl}" />
						</div>

						<div class="sl__item__contents">
							<div class="sl__item__contents__icon">
								<button>`;
									//좋아요 아이콘
									if(image.likeState){
										item +=`<i class="fas fa-heart active" style="padding-left:10px;" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
									}else{
										item +=`<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
									}
									
								item +=`
								</button>
							</div>
							
							<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>
					
							<div class="sl__item__contents__content" style="padding-bottom:15px;">
								<p>${image.caption}</p>
							</div>
							<div class="sl__item__contents__tags">`;
							
									let arr = image.tag.split('  ');
							
									for(let i = 0; i < arr.length; i++){
										item +=`<span class="tag-span" onclick="location.href='/image/search?tag=${arr[i]}'">#${arr[i]}</span>`;
									}
							
								item +=`</div><div id="storyCommentList-${image.id}">`;
								
								image.comment.forEach((comment)=>{
									item +=`<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
													<p>
														<b><a href="/user/${comment.user.id}">${comment.user.username}</a> :</b> ${comment.content}
													</p>`;
					
									if(principalId == comment.user.id){
										item +=`<button onclick="deleteComment(${comment.id})">
														<i class="fas fa-times"></i>
													 </button>`;
									}
									
									item +=`
									</div>
									<span class="commentDate" id="commentDate-${comment.id}"}>${comment.createDate}</span>`;
								});
								
							item +=`
						</div>
							<div class="sl__item__input">
								<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
								<button type="button" onClick="addComment(${image.id})">게시</button>
							</div>
					</div>
					<div class="modal-info" onclick="modalStory()">
						<div class="modal">
							<button onclick="storyDelete(deleteId)"><span style="color:red;"><strong>삭제</strong></span></button>
							<button onclick="closePopup('.modal-story')">취소</button>
						</div>
					</div>
				</div>
					`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	/*console.log("윈도우 scrollTop",$(window).scrollTop());
	console.log("문서의 높이",$(document).height());
	console.log("윈도우 높이",$(window).height());*/
	
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	
	if(checkNum <1 && checkNum > -1){
		page++;
		searchLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`); //$ 문법때문에 백틱으로 바꿔주기 
	
	if (likeIcon.hasClass("far")) { //좋아요 하겠다
		
		$.ajax({
			type:"post",
			url:`/api/image/${imageId}/likes`,
			dataType:"json"
		}).done(res=>{
			
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1; //number로 캐스팅
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			
			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
			
		}).fail(error=>{
			console.log("오류",error);
		});
		
		
	} else { //좋아요취소 하겠다
	
		$.ajax({
			type:"delete",
			url:`/api/image/${imageId}/likes`,
			dataType:"json"
		}).done(res=>{
			
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) - 1; //number로 캐스팅
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			
			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(error=>{
			console.log("오류",error);
		});
		
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId: imageId,
		content: commentInput.val()
	}

	if (data.content === "") { //프론트 단에서 막기
		alert("댓글을 작성해주세요!");
		return;
	}
	if (data.content.length > 100) { //프론트 단에서 막기
		alert("댓글은 100자 이내로 작성해 주세요.");
		return;
	}
	
	$.ajax({
		type:"post",
		url:"/api/comment",
		data:JSON.stringify(data), //현재 data는 자바스크립트 데이터이기에 json형태로 변환해줘야됨
		contentType:"application/json;charset=utf-8",
		dataType:"json"
	}).done(res=>{
		console.log("성공",res);
		
		let comment = res.data;
		
	let content = `
		  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
		    <p>
		      <b><a href="/user/${comment.user.id}">${comment.user.username}</a> :</b>
		      ${comment.content}
		    </p>
		    <button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
		  </div>
		  <span class="commentDate" id="commentDate-${comment.id}"}>${comment.createDate}</span>`;
		commentList.prepend(content);
		
	}).fail(error=>{
		console.log("오류",error.responseJSON.data.content);
		alert(error.responseJSON.data.content); //백 단에서 막기
	})

	
	commentInput.val(""); //input필드를 깨끗하게 비워줌
}

// (5) 댓글 삭제
function deleteComment(commentId) {

	$.ajax({
		type:"delete",
		url:`/api/comment/${commentId}`,
		dataType:"json"
	}).done(res=>{
		console.log("성공",res);
		$(`#storyCommentItem-${commentId}`).remove();
		$(`#commentDate-${commentId}`).remove();
	}).fail(error=>{
		console.log("실패",error);
	})
}	
// (6) 스토리 삭제
function storyDelete(imageId) {
		$.ajax({
		type:"delete",
		url:`/api/image/${imageId}`,
		dataType:"json"
	}).done(res=>{
		console.log("성공",res);
		$(`#storyListItem-${imageId}`).remove();
		window.location.replace("/");
	}).fail(error=>{
		console.log("실패",error);
	})
}

function popup(obj, imageId) {
	deleteId = imageId;
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}

function modalStory() {
	$(".modal-info").css("display", "none");
}

function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}





