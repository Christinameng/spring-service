package org.apache.servicecomb.samples.springmvc.provider;

import java.sql.*;

public class Database {
    private String url;

    public Database(String fileName) {
        String url = "jdbc:sqlite:./db/" + fileName;
        this.url = url;
        String customerTableSql = "CREATE TABLE IF NOT EXISTS customers ("
                + "	id integer PRIMARY KEY,"
                + "	name text NOT NULL,"
                + "	level INTEGER NOT NULL"
                + ");";
        String resourceTableSql = "CREATE TABLE IF NOT EXISTS resources ("
                + "	id integer PRIMARY KEY,"
                + "	state integer NOT NULL,"
                + "	customer_id INTEGER NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(customerTableSql);
            stmt.execute(resourceTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
