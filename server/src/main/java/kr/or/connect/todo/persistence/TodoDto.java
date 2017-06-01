package kr.or.connect.todo.persistence;

import java.util.Date;

public class TodoDto {
	private int id;
	private String todo;
	private int completed;
	private Date date;
	
	// completed, date 필드의 NOT NULL 조건으로 인한 오류 발생 방지를 위해 디폴트 값 설정.
	public TodoDto() {
		this.completed = 0;
		this.date = new Date();
	}
	
	public TodoDto(String todo) {
		this();
		this.todo = todo;
	}
	
	public TodoDto(int id, String todo, int completed, Date date) {
		this.id = id;
		this.todo = todo;
		this.completed = completed;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Todo [id=" +id+ ", todo=" +todo+ ", completed=" +completed+ ", date=" +date +"]";
	}
}
