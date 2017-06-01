package kr.or.connect.todo.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class TodoDao {
	private NamedParameterJdbcTemplate jdbc;
	private SimpleJdbcInsert insertAction;

	//BeanPropertyRowMapper.newInstance()로 생성한 객체는 멀티스레드에서 접근해도 안전하기 때문에 아래와 같이 멤버 변수로 선언하고 바로 초기화를 해도 무방하다.
	private RowMapper<TodoDto> rowMapper = BeanPropertyRowMapper.newInstance(TodoDto.class);

	@Autowired
	public TodoDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource)
				.withTableName("todo")
				.usingGeneratedKeyColumns("id");				
	}
	
	/**
	 * 할 일의 개수를 가져온다.(completed=0인 Todo)
	 * @return : 할 일의 개수
	 */
	public int getCount() {
		Map<String, Integer> params = Collections.singletonMap("completed", 0);
		return jdbc.queryForObject(TodoSqls.SELECT_COUNT_TODO, params, Integer.class);
	}
	
	/**
	 * 하나의 Todo 아이템을 가져온다.
	 * @param id
	 * @return
	 */
	public TodoDto selectOne(int id) {
		Map<String, Integer> params = Collections.singletonMap("id", id);
		return jdbc.queryForObject(TodoSqls.SELECT_BY_ID, params, rowMapper);
	}
	
	/**
	 * 모든 Todo 아이템을 가져온다.
	 * @return
	 */
	public List<TodoDto> selectAll() {
		Map<String, Object> params = Collections.emptyMap();
		return jdbc.query(TodoSqls.SELECT_TODO_ALL, params, rowMapper);
	}
	
	/**
	 * 필터링 된 Todo 아이템을 가져온다.
	 * @param filter : completed 값 : 0(active) OR 1(completed)
	 * @return
	 */
	public List<TodoDto> selectAll(int filter) {
		if(filter==-1) {
			return selectAll();
		}
		Map<String, Integer> params = Collections.singletonMap("completed", filter);
		return jdbc.query(TodoSqls.SELECT_TODO_BY_completed, params, rowMapper);
	}
	
	/**
	 * 하나의 Todo 아이템을 삽입한다.
	 * SimpleJdbcInsert를 활용한 INSERT 코드
	 * @param todoDto
	 * @return
	 */
	public Integer insert(TodoDto todoDto) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(todoDto);
		return insertAction.executeAndReturnKey(params).intValue();
	}

	/**
	 * 하나의 Todo 아이템을 수정한다. (완료/미완료 변경)
	 * @param completed : 1(완료) OR 0(미완료)
	 * @return
	 */
	public int update(int id, int completed) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("completed", completed);
		return jdbc.update(TodoSqls.UPDATE_TODO_BY_completed, params);
	}
	
	/**
	 * 하나의 Todo 아이템을 삭제한다.
	 * @param id : 삭제할 아이템의 번호
	 */
	public int delete(int id) {
		Map<String, Integer> params = Collections.singletonMap("id", id);
		return jdbc.update(TodoSqls.DELETE_TODO, params);
	}
	
	/**
	 * 완료된 Todo 아이템을 모두 삭제한다.
	 * @param id : 삭제할 아이템의 번호
	 */
	public int deleteCompletedTodo() {
		Map<String, Object> params = Collections.emptyMap();
		return jdbc.update(TodoSqls.DELETE_COMPLETED_TODO, params);
	}
		
}
