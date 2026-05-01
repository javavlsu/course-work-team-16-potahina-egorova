package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.FriendRequest;
import ru.vlsu.ispi.dao.FriendRequestDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FriendRequestDAOImpl extends HibernateDaoSupport implements FriendRequestDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(FriendRequest friendRequest){
        getHibernateTemplate().save(friendRequest);
    }

    public void update(FriendRequest friendRequest){
        getHibernateTemplate().update(friendRequest);
    }

    public void delete(FriendRequest friendRequest){
        getHibernateTemplate().delete(friendRequest);
    }

    public FriendRequest findById(Integer id){
        List list = getHibernateTemplate().find(
                "from friend_request where id=?", id
        );
        return (FriendRequest)list.get(0);
    }
}
