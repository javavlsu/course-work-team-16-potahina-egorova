package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.VisualMedia;
import ru.vlsu.ispi.dao.VisualMediaDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VisualMediaDAOImpl extends HibernateDaoSupport implements VisualMediaDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(VisualMedia visualMedia){
        getHibernateTemplate().save(visualMedia);
    }

    public void update(VisualMedia visualMedia){
        getHibernateTemplate().update(visualMedia);
    }

    public void delete(VisualMedia visualMedia){
        getHibernateTemplate().delete(visualMedia);
    }

    public VisualMedia findById(Integer id){
        List list = getHibernateTemplate().find(
                "from visualMedia where id=?", id
        );
        return (VisualMedia)list.get(0);
    }
}
