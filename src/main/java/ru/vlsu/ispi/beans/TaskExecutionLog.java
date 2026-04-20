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

	@OneToOne(cascade = CascadeType.MERGE)
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="music_media_id")
	private MusicMedia musicMedia;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="visual_media_id")
	private VisualMedia visualMedia;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="timer_mode_id")
	private TimerMode timerMode;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startTime;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endTime;

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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(LocalDateTime assignedAt) {
		this.assignedAt = assignedAt;
	}

	public boolean getIsReportAttached() {
		return isReportAttached;
	}

	public void setIsReportAttached(boolean reportAttached) {
		isReportAttached = reportAttached;
	}

	public String getCompletionReport() {
		return completionReport;
	}

	public void setCompletionReport(String completionReport) {
		this.completionReport = completionReport;
	}

	public MusicMedia getMusicMedia() {
		return musicMedia;
	}

	public void setMusicMedia(MusicMedia musicMedia) {
		this.musicMedia = musicMedia;
	}

	public VisualMedia getVisualMedia() {
		return visualMedia;
	}

	public void setVisualMedia(VisualMedia visualMedia) {
		this.visualMedia = visualMedia;
	}

	public TimerMode getTimerMode() {
		return timerMode;
	}

	public void setTimerMode(TimerMode timerMode) {
		this.timerMode = timerMode;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}