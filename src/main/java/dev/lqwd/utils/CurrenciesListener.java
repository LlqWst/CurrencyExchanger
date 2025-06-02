package dev.lqwd.utils;

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
        config.setJdbcUrl("jdbc:sqlite::resource:exchanger.db"); // бд в памяти
        config.setDriverClassName("org.sqlite.JDBC");

        config.addDataSourceProperty("foreign_keys", "true"); // Включает поддержку внешних ключей (FOREIGN KEY constraints) в SQLite
        config.addDataSourceProperty("journal_mode", "WAL"); // Режим журналирования изменений
        config.addDataSourceProperty("synchronous", "NORMAL"); // Настройка синхронизации записи на диск

        config.setMaximumPoolSize(10); // Максимальное количество соединений в пуле
        config.setConnectionTimeout(30000); // Время ожидания свободного соединения (30 секунд)
        config.setIdleTimeout(600000); // Время, после которого неиспользуемое соединение будет закрыто
        config.setMaxLifetime(1800000); // Максимальное время жизни соединения (даже если оно активно используется) (30 мин)
        config.setLeakDetectionThreshold(30000); // Если соединение не возвращается в пул дольше этого времени - логируется предупреждение
        return config;
    }

}
