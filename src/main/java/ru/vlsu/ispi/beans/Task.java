package ru.vlsu.ispi.beans; 

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@Column
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_list_id")
	private TaskList taskList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private enum Category {
		ImportantUrgent,
		UnimportantUrgent,
		ImportantNonUrgent,
		Other
	}

	private enum Status {
		NotStarted,
		Started,
		InProgress,
		Completed
	}

	@Enumerated(EnumType.STRING)
	@Column(name="category")
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;

	@Column
	private String details;
	@Column
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime deadlineAt;
	@Column
	private int points;

	public Task(int id, String title, TaskList taskList, User user,
				Category category, Status status, String details,
				LocalDateTime deadlineAt, int points) {
		this.id = id;
		this.title = title;
		this.taskList = taskList;
		this.user = user;
		this.category = category;
		this.status = status;
		this.details = details;
		this.deadlineAt = deadlineAt;
		this.points = points;
	}

	public Task() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task that = (Task) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

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

	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	} 

	public TaskList getTaskList() {
		return this.taskList;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setDetails(String details) {
		this.details = details; 
	} 

	public String getDetails() {
		return this.details; 
	}  

	public void setDeadlineAt(LocalDateTime deadlineAt) {
		this.deadlineAt = deadlineAt;
	} 

	public LocalDateTime getDeadlineAt() {
		return this.deadlineAt;
	} 

	public void setPoints(int points) {
		this.points = points; 
	} 

	public int getPoints() {
		return this.points; 
	}

	@Override
	public String toString() {
		return this.getId() + " " +
				this.title + " " +
				this.details + " " +
				this.deadlineAt + " " +
				this.points + " " +
				this.category.name() + " " +
				this.status.name();
	}
}