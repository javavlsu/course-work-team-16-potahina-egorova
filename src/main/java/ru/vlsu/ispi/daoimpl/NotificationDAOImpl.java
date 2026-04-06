package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.dao.NotificationDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NotificationDAOImpl extends HibernateDaoSupport implements NotificationDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(Notification notification){
        getHibernateTemplate().save(notification);
    }

    public void update(Notification notification){
        getHibernateTemplate().update(notification);
    }

    public void delete(Notification notification){
        getHibernateTemplate().delete(notification);
    }

    public Notification findById(Integer id){
        List list = getHibernateTemplate().find(
                "from notification where id=?", id
        );
        return (Notification)list.get(0);
    }
}
