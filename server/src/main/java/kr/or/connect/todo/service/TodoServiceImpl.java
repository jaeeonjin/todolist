package kr.or.connect.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.connect.todo.persistence.TodoDao;
import kr.or.connect.todo.persistence.TodoDto;

@Service("todoService")
public class TodoServiceImpl implements TodoService{

	private final TodoDao todoDao;

	@Autowired
	public TodoServiceImpl(TodoDao todoDao) {
		this.todoDao = todoDao;
	}

	@Override
	public int getTodoCount() {
		return todoDao.getCount();
	}
	
	@Override
	public TodoDto findOneById(Integer id) {
		return todoDao.selectOne(id);
	}

	/**
	 * -1일 때는 기본 selectAll.
	 * @param filter : -1(All), 0(Active), 1(Completed)
	 */
	@Override
	public List<TodoDto> findAllTodo(int filter) { 
		if(filter==-1) 
			return todoDao.selectAll();
		else
			return todoDao.selectAll(filter);
	}

	@Override
	public Integer insertTodo(TodoDto todoDto) {
		Integer id = todoDao.insert(todoDto);
		return id;
	}

	@Override
	public boolean updateTodo(Integer id, int completed) {
		Integer affected  = todoDao.update(id, completed);
		return affected == 1;
	}

	@Override
	public boolean deleteTodo(Integer id) {
		Integer affected  = todoDao.delete(id);
		return affected == 1;
	}

	@Override
	public void deleteCompletedTodo() {
		todoDao.deleteCompletedTodo();
	}

}
