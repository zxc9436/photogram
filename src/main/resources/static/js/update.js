// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); //폼태그 액션을 막기!!!
	
	let data = $("#profileUpdate").serialize(); //입력된 모든 엘리먼트를 문자열의 데이터에 serialize한다(key=value형태)
	
	$.ajax({
		type:"put",
		url:`/api/user/${userId}`, 
		data:data,
		contentType:"application/x-www-form-urlencoded; charset=utf-8", //현 데이터 타입을 알려줌
		dataType:"json" //응답을 받을 데이터 타입을 알려줌
	}).done(res=>{ //httpstatus 상태코드 200번대
		console.log("성공",res);
		location.href=`/user/${userId}`;
	}).fail(error=>{ //httpstatus 상태코드 200번대 아닐때
		if(error.data==null){
			alert(error.responseJSON.message);
		}else{
			alert(JSON.stringify(error.responseJSON.data)); //JSON.stringify : object를 json문자열로 변환해줌
		}
		
	});
}