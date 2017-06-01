package kr.or.connect.todo.persistence;

public class TodoSqls {
	
	// Todo(completed=0) 개수 선택
	static final String SELECT_COUNT_TODO =
			"SELECT COUNT(*) " +
			"FROM todo " +
			"WHERE completed = :completed";
	
	// Todo 선택 ( 1개 )
	static final String SELECT_BY_ID =
			"SELECT todo, completed, id " +
			"FROM todo " +
			"WHERE id= :id";
		
	// Todo 선택 ( 완료 Todos, 미완료 Todos )
	static final String SELECT_TODO_BY_completed =
			"SELECT todo, completed, id " +
			"FROM todo " +
			"WHERE completed= :completed " +
			"ORDER BY id  DESC";
	
	// Todo 선택 ( 전체 )
	static final String SELECT_TODO_ALL =
			"SELECT todo, completed, id " +
			"FROM todo " +
			"ORDER BY id DESC";
	
	// Todo 추가
	static final String INSERT_TODO =
			  "INSERT INTO todo " +
			  "(id, todo) " +
			  "VALUES (:id, :todo)";

	// Todo 수정 ( 완료, 미완료 )
	static final String UPDATE_TODO_BY_completed =
			"UPDATE todo " +
			"SET completed= :completed " +
			"WHERE id= :id";
	
	// Todo 삭제
	static final String DELETE_TODO =
			"DELETE FROM todo "+ 
			"WHERE id= :id";
	
	// 완료된 Todo 삭제
	static final String DELETE_COMPLETED_TODO = 
			"DELETE FROM todo "+ 
			"WHERE completed= 1";

}
