package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String email;
	@Column
	private String phoneNumber;
	@Column
	private int totalPoints = 0;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskList> taskLists = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Notification> notifications = new ArrayList<>();

	public User(String username, String password, String email,
				String phone_number, int total_points) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phone_number;
		this.totalPoints = total_points;
	}

	public User() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User that = (User) o;
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

	public void setUsername(String username) {
		this.username = username;
	} 

	public String getUsername() {
		return this.username;
	} 

	public void setPassword(String password) {
		this.password = password; 
	} 

	public String getPassword() {
		return this.password; 
	} 

	public void setEmail(String email) {
		this.email = email; 
	} 

	public String getEmail() {
		return this.email; 
	} 

	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
	} 

	public String getPhoneNumber() {
		return this.phoneNumber;
	}  

	public void setTotalPoints(int total_points) {
		this.totalPoints = total_points;
	} 

	public int getTotalPoints() {
		return this.totalPoints;
	}  

//	public String toString() {
//		return this.getId() + " " + this.name + " " + this.totalPoints;
//	}

	@Override
	public String toString() {
		return username != null ? username : "Неизвестный пользователь";
	}
}