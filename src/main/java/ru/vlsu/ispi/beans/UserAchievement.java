package ru.vlsu.ispi.beans; 

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "userAchievement")
public class UserAchievement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_id")
	private Task task;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "achievement_id")
	private Achievement achievement;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime achievedDate;

	public UserAchievement(User user, Task task, Achievement achievement,
						   LocalDateTime achievedDate) {
		this.user = user;
		this.task = task;
		this.achievement = achievement;
		this.achievedDate = achievedDate;
	}

	public UserAchievement() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserAchievement that = (UserAchievement) o;
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

	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	} 

	public Achievement getAchievement() {
		return this.achievement;
	} 

	public void setAchievedDate(LocalDateTime achievedDate) {
		this.achievedDate = achievedDate;
	} 

	public LocalDateTime getAchievedDate() {
		return this.achievedDate;
	}
}