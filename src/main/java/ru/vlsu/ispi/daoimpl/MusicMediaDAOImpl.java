package ru.vlsu.ispi.daoimpl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import ru.vlsu.ispi.beans.MusicMedia;
import ru.vlsu.ispi.dao.MusicMediaDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MusicMediaDAOImpl extends HibernateDaoSupport implements MusicMediaDAO {
    private DataSource dataSource;

    // Сеттер для внедрения DataSource через Spring XML конфигурацию
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Вспомогательный метод для получения соединения из пула
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void save(MusicMedia musicMedia){
        getHibernateTemplate().save(musicMedia);
    }

    public void update(MusicMedia musicMedia){
        getHibernateTemplate().update(musicMedia);
    }

    public void delete(MusicMedia musicMedia){
        getHibernateTemplate().delete(musicMedia);
    }

    public MusicMedia findById(Integer id){
        List list = getHibernateTemplate().find(
                "from musicMedia where id=?", id
        );
        return (MusicMedia)list.get(0);
    }
}
