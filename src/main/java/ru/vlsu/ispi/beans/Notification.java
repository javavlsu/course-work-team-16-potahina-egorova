package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private User user_id;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private Task task_id;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private UserAchievement user_achievement_id;
	@Column
	private String title;
	@Column
	private String text;
	@Column
	private boolean is_read;

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

	public void setUserId(User user_id) {
		this.user_id = user_id; 
	} 

	public User getUserId() {
		return this.user_id; 
	} 

	public void setTaskId(Task task_id) {
		this.task_id = task_id; 
	} 

	public Task getTaskId() {
		return this.task_id; 
	}

	public void setUserAchievementId(UserAchievement user_achievement_id) {
		this.user_achievement_id = user_achievement_id;
	}

	public UserAchievement getUserAchievementId() {
		return this.user_achievement_id;
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

	public void setIsRead(boolean is_read) {
		this.is_read = is_read; 
	} 

	public boolean getIsRead() {
		return this.is_read; 
	} 
}