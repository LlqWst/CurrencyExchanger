package dev.lqwd.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class CurrenciesListener implements ServletContextListener {

    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            HikariConfig config = getConfig();
            dataSource = new HikariDataSource(config);

            ServletContext ctx = sce.getServletContext();
            ctx.setAttribute("DBConnection", dataSource);
            ctx.setRequestCharacterEncoding("UTF-8");
            ctx.setInitParameter("defaultContentType", "application/json; charset=UTF-8");
        } catch (Exception e) {
            throw new RuntimeException();
        }
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

    private static HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db"); // для IDEA
        //config.setJdbcUrl("jdbc:sqlite::resource:exchanger.db"); // для сервера
        config.setDriverClassName("org.sqlite.JDBC");

        config.addDataSourceProperty("foreign_keys", "true");
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("synchronous", "NORMAL");

        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(30000);
        return config;
    }

}
