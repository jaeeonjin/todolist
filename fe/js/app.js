
/**
 * Todo 리스트를 출력한다. (초기 필터 : ALL)
 */
window.onload = function() {
	'use strict';
	var filter = $(".selected")[0]; // 현재 선택된 필터. 첫 로딩시 [All]
	showFilteredList(filter); // 리스트 출력
} 

/**
 * 필터링 된 Todo 리스트를 출력한다.
 * @param object : 선택 Filter
 */
function showFilteredList(object) {
	var id = object.id; // -1(All), 0(Active), 1(Completed)
	var className = $("#"+id).attr("class"); // selected, not_selected

	// 1. 클래스명 변경
	//  기존 노드의 클래스 수정 : selected -> not_selected
	$(".selected").attr("class","not_selected");
	
	// 2.클래스명 변경
	//  현재 선택 노드의 클래스 수정 : not_selected->selected
	$("#"+id).attr("class","selected");
	
	// 3. Ajax 호출 및 비동기 출력
	$.ajax({
		url: getContextPath()+ "/api/todos/"+id,
		type: "GET"
	}).done(function (data) {
		if(data.list != null) {
			$('.todo-list').empty(); // 먼저 리스트를 비운다.
			
			// Todo가 Completed인지 파악한 뒤
			// View에 출력한다.(getAppendNode 함수를 호출한다.
			for(var i=0; i<data.list.length; i++) {
				var liClassName
					= data.list[i]["completed"] == 1? "completed" : "not_completed"; 
 
				$('.todo-list').append (
						appendTodo(liClassName, data.list[i])
				);
			}
		
			// 모든 출력이 완료된 후 전체 개수를 갱신한다.
			refreshListSize();
		}
	});
}

/**
 * 할 일을 추가한다.
 * @param input : 할 일을 입력하는 INPUT 노드
 */
function addTodo(input) {
	
	if(event.keyCode == 13) { // 엔터키 인식
		
		if( input.value == "") { // 아무것도 입력하지 않고 엔터키를 눌렀을 때 처리
			alert("할 일을 입력해주세요.");
			return;
		}

		var param = {"todo" : input.value}; // api로 보낼 데이터
		$.ajax({
			url: getContextPath()+ "/api/todos/create",
			type: "POST",
			data: param
		}).done(function (data) {
			$('.todo-list')
			.prepend (
					appendTodo("not_completed", data.object)
			);	
			input.value = ""; // INPUT 값 초기화
			
			// 필터가 Completed(id : 1)인 경우는
			// 할 일을 추가했을 때 해당 리스트에 그대로 출력된다.
			// 따라서 해당 필터에 맞는 리스트를 다시 출력한다.
			var filter = $(".selected")[0];
			if( filter.id == 1 )
				showFilteredList(filter); 
			
			// 출력이 완료된 후 전체 개수를 갱신한다.
			refreshListSize(); 
		});
	}
}

/**
 * 토글(체크박스) 버튼 클릭 시, 완료/미완료 상태를 변경한다.
 * @param object : 체크박스 객체
 */
function checkToggle(object) {
	var flag = object.checked; // 토글 버튼을 누른 다음의 값이 저장된다.
	
	// 미완료, 완료 여부를 결정한다.
	var param;
	if(flag) { // 미체크 -> 체크
		$(object).parent().parent().attr("class", "completed");
		param = {"completed":1}; 
	} else { // 체크 -> 미체크
		$(object).parent().parent().attr("class", "not_completed");
		param = {"completed":0};
	}

	$.ajax({
		url: getContextPath()+ "/api/todos/update/"+object.id,
		type: "PUT",
		data: param
	}).done(function () {		

		//   필터가 Completed(id : 1 OR 2) 인 경우에..
		//   할 일을 추가했을 때 해당 리스트에 그대로 출력된다.
		//   따라서 해당 필터에 맞는 리스트를 다시 출력한다.
		var filter = $(".selected")[0];
		if( filter.id == 1 || filter.id == 0 )
			showFilteredList(filter); 
		
		refreshListSize();
	});
}

/**
 * X(삭제) 버튼 클릭시 해당 아이템을 삭제한다.
 * @param object : 삭제할 노드
 */
function removeTodo(object) {
	var id = object.previousSibling.previousSibling.id;
	$.ajax({
		url: getContextPath()+ "/api/todos/delete/"+id,
		type: "DELETE"
	}).done(function () {
		$("#"+id).closest("li").remove();
		refreshListSize();
	});;
}

/**
 * 완료된 아이템을 모두 삭제한다.
 */
function removeCompletedTodo() {
	$.ajax({
		url: getContextPath()+ "/api/todos/delete/completedTodo",
		type: "DELETE"
	}).done(function () {
		$('.todo-list').children(".completed").remove();
		refreshListSize();
	})
}

/**
 * (아직 완료하지 못한) 할 일의 갯수를 갱신하다.
 */
function refreshListSize() {
	$.ajax({
		url: getContextPath()+ "/api/todos/count",
		type: "GET"
	}).done(function (data) {
		var size = data.count;
		$(".todo-count").children().first().remove();
		$(".todo-count").prepend("<strong>" + size + "</strong>");
	})
}

/**
 * 추가할 Todo 노드 HTML 코드를 반환하는 메소드
 * @returns : Todo 노드의 HTML 코드
 */
function appendTodo(className, data) {
	var checked
		= className=="completed" ? "checked" : "";

	var result = 
		"<li class=\" " + className + " \"> " +
		"<div class=\"view\">" +
		"<input class=\"toggle\" type=\"checkbox\" onclick=\"checkToggle(this);\" id=\"" + data["id"] + "\"" + checked + " >" +
		"<label> " + data["todo"] + "</label>" +
		"<button class=\"destroy\" onclick=\"removeTodo(this);\"></button>" +
		"</	div>" +
		"</li>";
	return result;
}

/**
 * BASE_URL 반환 FUNCTION
 * @returns : Ex) http://localhost:8080
 */
function getContextPath() {
	var hostIndex = location.href.indexOf( location.host ) + location.host.length;
	return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
}

