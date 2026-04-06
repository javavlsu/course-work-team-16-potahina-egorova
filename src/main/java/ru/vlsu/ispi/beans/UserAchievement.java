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
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private User user_id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="task_id")
	private Task task;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private Achievement achievement_id;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime achieved_date;

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

	public void setUserId(User user_id) {
		this.user_id = user_id; 
	} 

	public User getUserId() {
		return this.user_id; 
	}

	public void setTaskId(Task task_id) {
		this.task = task_id;
	}

	public Task getTaskId() {
		return this.task;
	}

	public void setAchievementId(Achievement achievement_id) {
		this.achievement_id = achievement_id; 
	} 

	public Achievement getAchievementId() {
		return this.achievement_id; 
	} 

	public void setAchievedDate(LocalDateTime achieved_date) {
		this.achieved_date = achieved_date; 
	} 

	public LocalDateTime getAchievedDate() {
		return this.achieved_date; 
	}
}