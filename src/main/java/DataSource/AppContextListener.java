package DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("org.sqlite.JDBC");

        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);

        dataSource = new HikariDataSource(config);
        sce.getServletContext().setAttribute("DBConnection", dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(dataSource != null){
            dataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

}
