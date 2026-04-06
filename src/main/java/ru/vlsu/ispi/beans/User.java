package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String name;
	@Column
	private String password;
	@Column
	private String email;
	@Column
	private String phoneNumber;
	@Column
	private int totalPoints = 0;

	public User(String name, String password, String email, String phone_number, int total_points) {
		this.name = name;
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

	public void setName(String name) {
		this.name = name; 
	} 

	public String getName() {
		return this.name; 
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

	public String toString() {
		return this.getId() + " " + this.name + " " + this.totalPoints;
	}
}