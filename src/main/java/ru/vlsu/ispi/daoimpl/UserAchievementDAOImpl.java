package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.UserAchievement;
import ru.vlsu.ispi.dao.UserAchievementDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserAchievementDAOImpl extends HibernateDaoSupport implements UserAchievementDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(UserAchievement userAchievement){
        getHibernateTemplate().save(userAchievement);
    }

    public void update(UserAchievement userAchievement){
        getHibernateTemplate().update(userAchievement);
    }

    public void delete(UserAchievement userAchievement){
        getHibernateTemplate().delete(userAchievement);
    }

    public UserAchievement findById(Integer id){
        List list = getHibernateTemplate().find(
                "from userAchievement where id=?", id
        );
        return (UserAchievement)list.get(0);
    }
}
