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
	@Column
	private int reward_points;

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

	public void setRewardPoints(int reward_points) {
		this.reward_points = reward_points; 
	} 

	public int getRewardPoints() {
		return this.reward_points; 
	} 
}