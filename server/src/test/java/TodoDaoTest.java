
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.todo.TodoApplication;
import kr.or.connect.todo.persistence.TodoDao;
import kr.or.connect.todo.persistence.TodoDto;

/**
 * DAO 테스트
 * ************
 * TC1 : Insert
 * TC2 : Delete
 * TC3 : Update
 * TC4 : Select(count) 
 * TC5 : SelectAll
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TodoApplication.class)
@Transactional
public class TodoDaoTest {

	@Autowired
	private TodoDao dao;

	//TC1
	@Test 
	public void shouldInsert() { 
		// GIVEN
		TodoDto todoDto = new TodoDto("INSERT테스트");

		// WHEN
		Integer id = dao.insert(todoDto);

		// THEN
		TodoDto selected = dao.selectOne(id);
		assertThat(selected.getTodo(), is("INSERT테스트"));
	}

	//TC2
	@Test
	public void shouldDelete() { 
		// GIVEN
		TodoDto todoDto = new TodoDto("DELETE테스트");
		Integer id = dao.insert(todoDto);
		TodoDto selected = dao.selectOne(id);
		
		// WHEN
		Integer num = dao.delete(selected.getId());
		
		// THEN
		assertThat(num, is(1));
	}

	//TC3
	@Test
	public void shouldUpdate() { 
		// GIVEN
		TodoDto todoDto = new TodoDto("UPDATE테스트");
		Integer id = dao.insert(todoDto);
		TodoDto selected = dao.selectOne(id);
		
		// WHEN
		int completed = selected.getCompleted(); // 새로 만든 Todo 이므로 completed 값은 0이다.
		Integer num = dao.update(selected.getId(), 1);	
		
		// THEN
		assertThat(completed, is(0));
		assertThat(num, is(1));
	}

	//TC4
	@Test
	public void shouldSelect_count() { 
		// GIVEN
		int beforeCount = dao.getCount();
		System.out.println("beforeCount : " + beforeCount );
		dao.insert(new TodoDto("SELECT(count) 테스트"));
		
		// WHEN - 할 일을 추가했으므로 afterCount == (beforeCount+1)
		int afterCount = dao.getCount();
		
		// THEN
		assertThat(afterCount, is(beforeCount+1));
	}

	//TC5
	@Test
	public void shouldSelect_All() { 
		// GIVEN
		List<TodoDto> beforeList = dao.selectAll();
		dao.insert(new TodoDto("SELECTALL 테스트"));
		
		// WHEN - 할 일을 추가했으므로 afterList.size() == ( beforeList.size() + 1 ) 
		List<TodoDto> afterList = dao.selectAll();

		// THEN
		assertThat(afterList.size(), is(beforeList.size()+1));
		
	}
	
}
