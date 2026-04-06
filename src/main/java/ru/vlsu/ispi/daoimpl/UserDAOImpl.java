package ru.vlsu.ispi.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.dao.UserDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl extends HibernateDaoSupport implements UserDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(User user){
        getHibernateTemplate().save(user);
    }

    public void update(User user){
        getHibernateTemplate().update(user);
    }

    public void delete(User user){
        getHibernateTemplate().delete(user);
    }

    public User findById(Integer id){
        List list = getHibernateTemplate().find(
                "from user where user_id=?", id
        );
        return (User)list.get(0);
    }

    @Override
    public List<User> findAll(){
        return getHibernateTemplate().loadAll(User.class); // Получаем всех пользователей
    }
}
