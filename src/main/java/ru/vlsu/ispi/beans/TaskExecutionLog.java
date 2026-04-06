package ru.vlsu.ispi.beans;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "taskExecutionLog")
public class TaskExecutionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn
	private Task task;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="assigned_to_user_id")
	private User user;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime assignedAt;
	@Column
	private boolean isReportAttached;
	@Column
	private String completionReport;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="music_media_id")
	private MusicMedia musicMedia;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="visual_media_id")
	private VisualMedia visualMedia;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="timer_mode_id")
	private TimerMode timerMode;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime start_time;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime end_time;

	public TaskExecutionLog() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TaskExecutionLog that = (TaskExecutionLog) o;
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

	public void setMusicMediaId(MusicMedia musicMedia) {
		this.musicMedia = musicMedia;
	} 

	public MusicMedia getMusicMediaId() {
		return this.musicMedia;
	}  

	public void setVisualMediaId(VisualMedia visualMedia) {
		this.visualMedia = visualMedia;
	} 

	public VisualMedia getVisualMediaId() {
		return this.visualMedia;
	} 

	public void setTimerModeId(TimerMode timerMode) {
		this.timerMode = timerMode;
	} 

	public TimerMode getTimerModeId() {
		return this.timerMode;
	} 
}