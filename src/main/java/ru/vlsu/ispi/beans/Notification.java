package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "user_achievement_id")
	private UserAchievement userAchievement;

	@Column
	private String title;
	@Column
	private String text;
	@Column(name = "is_read")
	private boolean isRead;
	@Column
	private LocalDateTime createdAt;

	public Notification(User user, Task task, UserAchievement userAchievement,
						String title, String text, boolean isRead,
						LocalDateTime createdAt) {
		this.user = user;
		this.task = task;
		this.userAchievement = userAchievement;
		this.title = title;
		this.text = text;
		this.isRead = isRead;
		this.createdAt = createdAt;
	}

	public Notification() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Notification that = (Notification) o;
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

	public void setUser(User user) {
		this.user = user;
	} 

	public User getUser() {
		return this.user;
	} 

	public void setTask(Task task) {
		this.task = task;
	} 

	public Task getTask() {
		return this.task;
	}

	public void setUserAchievement(UserAchievement userAchievement) {
		this.userAchievement = userAchievement;
	}

	public UserAchievement getUserAchievement() {
		return this.userAchievement;
	}

	public void setTitle(String title) {
		this.title = title; 
	} 

	public String getTitle() {
		return this.title; 
	} 

	public void setText(String text) {
		this.text = text; 
	} 

	public String getText() {
		return this.text; 
	} 

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	} 

	public boolean getIsRead() {
		return this.isRead;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}