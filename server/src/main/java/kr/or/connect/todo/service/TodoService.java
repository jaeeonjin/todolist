package kr.or.connect.todo.service;

import java.util.List;

import kr.or.connect.todo.persistence.TodoDto;

public interface TodoService {

	// 할 일의 개수를 가져온다.(completed=0)
	int getTodoCount();
	
	// Todo 한 개 가져오기
	TodoDto findOneById(Integer id);
		
	// 필터링된 Todo 리스트 가져오기
	List<TodoDto> findAllTodo(int filter);

	// Todo 아이템 삽입하기
	Integer insertTodo(TodoDto todoDto);
	
	// Todo 아이템 수정하기(수정-completed)
	boolean updateTodo(Integer id, int completed);
	
	// Todo 아이템 삭제하기
	boolean deleteTodo(Integer id);
	
	// completed Todo 아이템 삭제하기
	void deleteCompletedTodo();
	
}
