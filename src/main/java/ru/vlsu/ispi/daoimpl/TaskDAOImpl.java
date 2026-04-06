package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.dao.TaskDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskDAOImpl extends HibernateDaoSupport implements TaskDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(Task task){
        getHibernateTemplate().save(task);
    }

    public void update(Task task){
        getHibernateTemplate().update(task);
    }

    public void delete(Task task){
        getHibernateTemplate().delete(task);
    }

    public Task findById(Integer id){
        List list = getHibernateTemplate().find(
                "from task where id=?", id
        );
        return (Task)list.get(0);
    }
}
