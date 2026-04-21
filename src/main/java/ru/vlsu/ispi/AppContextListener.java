package ru.vlsu.ispi;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


@WebListener
public class AppContextListener implements ServletContextListener {
    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/time_management");
        config.setUsername("root");
        config.setPassword("1825xy");
        // Другие настройки пула
        dataSource = new HikariDataSource(config);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null) {
            dataSource.close(); // Пул сам остановит все фоновые потоки
        }
        // Поток AbandonedConnectionCleanupThread завершится автоматически
    }
}
