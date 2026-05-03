package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.*;

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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_friends",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "friend_id")
	)
	private List<User> friends = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_friends",
			joinColumns = @JoinColumn(name = "friend_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> friendsOf = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskList> taskLists = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	@OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> assignedTasks = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Notification> notifications = new ArrayList<>();

	public User(String name, String password, String email,
				String phone_number, int total_points) {
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

	public List<User> getAllFriends() {
		Set<User> allFriends = new HashSet<>(friends);
		allFriends.addAll(friendsOf);
		return new ArrayList<>(allFriends);
	}

	public void setAllFriends(List<User> friendsOf) {
		this.friendsOf = friendsOf;
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

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return name != null ? name : "Неизвестный пользователь";
	}
}