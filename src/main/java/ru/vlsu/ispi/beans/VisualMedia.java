package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "visualMedia")
public class VisualMedia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String name;
	@Column
	private String url;

	public VisualMedia() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VisualMedia that = (VisualMedia) o;
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

	public void setUrl(String url) {
		this.url = url; 
	} 

	public String getUrl() {
		return this.url; 
	}
}