package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "achievement")
public class Achievement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String title;
	@Column
	private String description;
	@Column(name="reward_points")
	private int rewardPoints;

	public Achievement(String title, String description, int rewardPoints) {
		this.title = title;
		this.description = description;
		this.rewardPoints = rewardPoints;
	}

	public Achievement() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Achievement that = (Achievement) o;
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

	public void setDescription(String description) {
		this.description = description; 
	} 

	public String getDescription() {
		return this.description; 
	} 

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	} 

	public int getRewardPoints() {
		return this.rewardPoints;
	} 
}