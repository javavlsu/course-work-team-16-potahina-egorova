package ru.vlsu.ispi.beans; 

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_list_id")
	private TaskList taskList;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="assigned_to_user_id")
	private User assignedUser;

	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private List<Notification> notifications = new ArrayList<>();

	public enum Category {
		ImportantUrgent,
		UnimportantUrgent,
		ImportantNonUrgent,
		Other
	}

	public enum Status {
		NotStarted,
		Started,
		InProgress,
		Completed
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable = false)
	private Status status;

	@Column
	private String details;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime assignedAt;

	@Column
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime deadlineAt;
	@Column
	private int points;


	public Task(String title, TaskList taskList, User user,
				User assignedUser, Category category, Status status,
				String details, LocalDateTime assignedAt,
				LocalDateTime deadlineAt, int points) {
		this.title = title;
		this.taskList = taskList;
		this.user = user;
		this.assignedUser = assignedUser;
		this.category = category != null ? category : Category.Other;
		this.status = status != null ? status : Status.NotStarted;
		this.details = details;
		this.assignedAt = assignedAt;
		this.deadlineAt = deadlineAt;
		this.points = points;
	}

	public Task(Category category, Status status) {
		this.category = Category.Other;
		this.status = Status.NotStarted;
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
		return user;
	}

	public User getAssignedUser() {
		return this.assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	} 

	public TaskList getTaskList() {
		return this.taskList;
	}

	public void setCategory(Category category) {
		this.category = category != null ? category : Category.Other;
	}

	public Category getCategory() {
		return category;
	}

	public void setStatus(Status status) {
		this.status = status != null ? status : Status.NotStarted;
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

	public LocalDateTime getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(LocalDateTime assignedAt) {
		this.assignedAt = assignedAt;
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