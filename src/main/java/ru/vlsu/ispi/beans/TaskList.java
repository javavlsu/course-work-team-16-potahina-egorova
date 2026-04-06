package ru.vlsu.ispi.beans;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasklist")
public class TaskList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@Column
	@NotEmpty
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	public TaskList(int id, String title, User user){
		this.id = id;
		this.title = title;
		this.user = user;
	}

	public TaskList() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TaskList that = (TaskList) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public void setTitle(String title) {
		this.title = title; 
	} 

	public String getTitle() {
		return this.title; 
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}
}