
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import kr.or.connect.todo.TodoApplication;

/**
 * CONTROLLER 테스트
 * ************
 * TC1 : create
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@Transactional
public class TodoControllerTest {

	@Autowired
	WebApplicationContext wac;
	MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(this.wac)
				.alwaysDo(print(System.out))
				.build();
	}

	//TC1
	@Test
	public void shouldCreate() throws Exception {
		mvc.perform(
				post("/api/todos/create")
				.contentType(MediaType.APPLICATION_JSON)
				.param("todo", "테스트")
				)

		.andExpect(jsonPath("$.object.todo").exists())
		.andExpect(jsonPath("$.object.todo").value("테스트"));		
	}

}
