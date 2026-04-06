package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.dao.AchievementDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AchievementDAOImpl extends HibernateDaoSupport implements AchievementDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(Achievement achievement){
        getHibernateTemplate().save(achievement);
    }

    public void update(Achievement achievement){
        getHibernateTemplate().update(achievement);
    }

    public void delete(Achievement achievement){
        getHibernateTemplate().delete(achievement);
    }

    public Achievement findById(Integer id){
        List list = getHibernateTemplate().find(
                "from achievement where id=?", id
        );
        return (Achievement)list.get(0);
    }
}
