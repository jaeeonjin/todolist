package kr.or.connect.todo.api;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.connect.todo.persistence.TodoDto;
import kr.or.connect.todo.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private final TodoService todoService;
	
	@Autowired
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	/**
	 * Todo(completed=0)의 개수를 가져온다. 
	 * @return Todo(completed=0)의 개수 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/count")
	public JSONObject getCount() {
		int count = todoService.getTodoCount();
		
		JSONObject json = new JSONObject();
		json.put("count", count);		
		
		return json;
	}
	
	/**
	 * 필터링 된 Todo 리스트를 가져온다.
	 * @return : 필터링 된 리스트 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/{filter}")
	public JSONObject findAll(@PathVariable String filter) {
		List<TodoDto> list = todoService.findAllTodo(Integer.parseInt(filter));
		
		JSONObject json = new JSONObject();
		json.put("list", list);
		json.put("size", list.size());
		
		return json;
	}
	
	/**
	 * 사용자가 입력한 Todo를 DB에 등록한다.
	 * @param todoDto : 사용자 입력 데이터 (todo)
	 * @return todo를 반환 (Ajax .done(function ())의 argument로 사용된다.)
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value="/create")
	public JSONObject create(TodoDto todoDto) {
		int id = todoService.insertTodo(todoDto).intValue();
		TodoDto addedTodo = todoService.findOneById(id);
		
		JSONObject json = new JSONObject();
		json.put("object", addedTodo);

		return json;
	}
	
	/**
	 * Todo 아이템의 completed 속성을 업데이트한다.
	 * @param id : 변경할 아이템의 ID
	 * @param completed : 변경할 값 ( 1 or 0 )
	 */
	@PutMapping(value="/update/{id}")
	public void update(@PathVariable String id, @RequestParam("completed") String completed) {
		int paramId = Integer.parseInt(id);
		int paramCompleted = Integer.parseInt(completed);
		
		todoService.updateTodo(paramId, paramCompleted);
	}

	/**
	 * Todo 아이템을 삭제한다
	 * @param id : 삭제할 아이템의 아이디
	 */
	@DeleteMapping(value="/delete/{id}")
	public JSONObject delete(@PathVariable String id) {
		int paramId = Integer.parseInt(id);
		boolean flag = todoService.deleteTodo(paramId);
	
		JSONObject json = new JSONObject();
		json.put("flag", flag);
		return json;
	}
	
	/**
	 * 완료된 Todo 아이템을 모두 삭제한다. 
	 */
	@DeleteMapping(value="/delete/completedTodo")
	public void deleteAll() {
		todoService.deleteCompletedTodo();
	}
}
