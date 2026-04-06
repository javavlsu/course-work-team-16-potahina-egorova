package ru.vlsu.ispi.beans;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "timerMode")
public class TimerMode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String mode_name;
	@Column
	private int work_duration_min;
	@Column
	private int short_break_min;
	@Column
	private int long_break_min;
	@Column
	private String mode_description;

	public TimerMode() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TimerMode that = (TimerMode) o;
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

	public void setModeName(String mode_name) {
		this.mode_name = mode_name; 
	} 

	public String getModeName() {
		return this.mode_name; 
	} 

	public void setWorkDurationMin(int work_duration_min) {
		this.work_duration_min = work_duration_min; 
	} 

	public int getWorkDurationMin() {
		return this.work_duration_min; 
	} 

	public void setShortBreakMin(int short_break_min) {
		this.short_break_min = short_break_min; 
	} 

	public int getShortBreakMin() {
		return this.short_break_min; 
	} 

	public void setLongBreakMin(int long_break_min) {
		this.long_break_min = long_break_min; 
	} 

	public int getLongBreakMin() {
		return this.long_break_min; 
	}  

	public void setModeDescription(String mode_description) {
		this.mode_description = mode_description; 
	} 

	public String getModeDescription() {
		return this.mode_description; 
	} 
}