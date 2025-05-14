package org.example.sqlUtils;

import DataSource.CurrenciesListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtils {

    public boolean isExist(String code) throws SQLException {
        Connection conn = CurrenciesListener.getConnection();

        String query = "SELECT ID FROM Currencies WHERE Code = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, code);
        ResultSet rs = statement.executeQuery();
        return rs == null;

    }
}
