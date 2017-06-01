package kr.or.connect.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// 아래 어노테이션 : @Configuration, @ComponentScan이 포함된 어노테이션
// 따라서 별도로 AppConfig 파일을 생성하고 설정할 필요가 없다.
@SpringBootApplication 
public class TodoApplication extends WebMvcConfigurerAdapter {
	
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}
	
}
