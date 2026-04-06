package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.TimerMode;
import ru.vlsu.ispi.dao.AchievementDAO;
import ru.vlsu.ispi.dao.TimerModeDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TimerModeDAOImpl extends HibernateDaoSupport implements TimerModeDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(TimerMode timerMode){
        getHibernateTemplate().save(timerMode);
    }

    public void update(TimerMode timerMode){
        getHibernateTemplate().update(timerMode);
    }

    public void delete(TimerMode timerMode){
        getHibernateTemplate().delete(timerMode);
    }

    public TimerMode findById(Integer id){
        List list = getHibernateTemplate().find(
                "from timerMode where id=?", id
        );
        return (TimerMode)list.get(0);
    }
}
