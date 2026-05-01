package ru.vlsu.ispi.tmservice;

import ru.vlsu.ispi.dao.*;

public class TMService {
    private UserDAO userDAO;
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public UserDAO getUserDAO() {
        return userDAO;
    }

    private UserAchievementDAO userAchievementDAO;
    public void setUserAchievementDAO(UserAchievementDAO userAchievementDAO) {
        this.userAchievementDAO = userAchievementDAO;
    }
    public UserAchievementDAO getUserAchievementDAO() {
        return userAchievementDAO;
    }

    private AchievementDAO achievementDAO;
    public void setAchievementDAO(AchievementDAO achievementDAO) {
        this.achievementDAO = achievementDAO;
    }
    public AchievementDAO getAchievementDAO() {
        return achievementDAO;
    }

    private TaskDAO taskDAO;
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    public TaskDAO getTaskDAO() {
        return taskDAO;
    }

    private TaskListDAO taskListDAO;
    public void setTaskListDAO(TaskListDAO taskListDAO) {
        this.taskListDAO = taskListDAO;
    }
    public TaskListDAO getTaskListDAO() {
        return taskListDAO;
    }

    private TaskExecutionLogDAO taskExecutionLogDAO;
    public void setTaskExecutionLogDAO(TaskExecutionLogDAO taskExecutionLogDAO) {
        this.taskExecutionLogDAO = taskExecutionLogDAO;
    }
    public TaskExecutionLogDAO getTaskExecutionLogDAO() {
        return taskExecutionLogDAO;
    }

    private NotificationDAO notificationDAO;
    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }
    public NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }

    private TimerModeDAO timerModeDAO;
    public void setTimerModeDAO(TimerModeDAO timerModeDAO) {
        this.timerModeDAO = timerModeDAO;
    }
    public TimerModeDAO getTimerModeDAO() { return timerModeDAO; }

    private MusicMediaDAO musicMediaDAO;
    public void setMusicMediaDAO(MusicMediaDAO musicMediaDAO) {
        this.musicMediaDAO = musicMediaDAO;
    }
    public MusicMediaDAO getMusicMediaDAO() { return musicMediaDAO; }

    private VisualMediaDAO visualMediaDAO;
    public void setVisualMediaDAO(VisualMediaDAO visualMediaDAO) {
        this.visualMediaDAO = visualMediaDAO;
    }
    public VisualMediaDAO getVisualMediaDAO() { return visualMediaDAO; }

    private FriendRequestDAO friendRequestDAO;
    public void setFriendRequestDAO(FriendRequestDAO friendRequestDAO) {
        this.friendRequestDAO = friendRequestDAO;
    }
    public FriendRequestDAO getFriendRequestDAO() { return friendRequestDAO; }
}
