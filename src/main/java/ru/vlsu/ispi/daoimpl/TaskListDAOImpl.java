package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.dao.TaskListDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskListDAOImpl extends HibernateDaoSupport implements TaskListDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(TaskList taskList){
        getHibernateTemplate().save(taskList);
    }

    public void update(TaskList taskList){
        getHibernateTemplate().update(taskList);
    }

    public void delete(TaskList taskList){
        getHibernateTemplate().delete(taskList);
    }

    public TaskList findById(Integer id){
        List list = getHibernateTemplate().find(
                "from taskList where id=?", id
        );
        return (TaskList)list.get(0);
    }
}
