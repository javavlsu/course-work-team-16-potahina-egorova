package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskExecutionLog;
import ru.vlsu.ispi.dao.TaskDAO;
import ru.vlsu.ispi.dao.TaskExecutionLogDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskExecutionLogDAOImpl extends HibernateDaoSupport implements TaskExecutionLogDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(TaskExecutionLog taskExecutionLog){
        getHibernateTemplate().save(taskExecutionLog);
    }

    public void update(TaskExecutionLog taskExecutionLog){
        getHibernateTemplate().update(taskExecutionLog);
    }

    public void delete(TaskExecutionLog taskExecutionLog){
        getHibernateTemplate().delete(taskExecutionLog);
    }

    public TaskExecutionLog findById(Integer id){
        List list = getHibernateTemplate().find(
                "from taskExecutionLog where id=?", id
        );
        return (TaskExecutionLog)list.get(0);
    }
}
