package DataSource;

import java.sql.*;
import java.util.Scanner;

public class Program {

    Connection co;
    private static final String DB_PATH = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";
//    public static void main(String ... args) throws SQLException, ClassNotFoundException {
//
//        SqlUtils.Program program = new SqlUtils.Program();
//        program.open();

    /// /        program.insert();
//        program.select();
//        program.close();
//    }
    public void open() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC"); // инициализация драйвера (в скобках имя пакета)
        co = DriverManager.getConnection(DB_PATH); // устанавливаем связь с БД
        System.out.println("Connected");
    }

    void insert() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user name: ");
        String name = scanner.nextLine();
        System.out.println("Enter user phone: ");
        String phone = scanner.nextLine();

        String query =
                "PRAGMA foreign_keys = ON " +
                        "INSERT INTO users (name, phone) " +
                        "VALUES ('" + name + "', '" + phone + "') ";
        Statement statement = co.createStatement();
        statement.executeUpdate(query); // запрос может быть на получение и на обновление данных
        statement.close();
        System.out.println("Rows added");
    }

    public void close() throws SQLException {
        co.close();
    }

    public void select() throws SQLException {
        String query =
                "SELECT * FROM Currencies";
        Statement statement = co.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            int ID = rs.getInt("ID");
            String Code = rs.getString("Code");
            String FullName = rs.getString("FullName");
            String Sign = rs.getString("Sign");
            System.out.println("ID:" + ID + " | Code:" + Code + " | FullName:" + FullName + " | Sign:" + Sign);
        }
        rs.close();
        statement.close();
    }

    public StringBuilder select_1() throws SQLException, ClassNotFoundException {
        open();
        String query =
                "SELECT * FROM Currencies";
        Statement statement = co.createStatement();
        ResultSet rs = statement.executeQuery(query);
        StringBuilder line = new StringBuilder("");
        while (rs.next()) {
            int ID = rs.getInt("ID");
            String Code = rs.getString("Code");
            String FullName = rs.getString("FullName");
            String Sign = rs.getString("Sign");
            //System.out.println("ID:" + ID + " | Code:" + Code + " | FullName:" + FullName + " | Sign:" + Sign);
            line.append(String.format("ID:%d | Code:%s | FullName:%s | Sign:%s |<br>", ID, Code, FullName, Sign));
        }
        rs.close();
        statement.close();
        close();
        return line;
    }

}