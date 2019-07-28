package org.apache.servicecomb.samples.springmvc.provider;

import java.sql.*;

class CustomerDao extends Dao {
    CustomerDao(String fileName) {
        super(fileName);
        String customerTableSql = "CREATE TABLE IF NOT EXISTS customers ("
                + "	id integer PRIMARY KEY,"
                + "	name text NOT NULL,"
                + "	level INTEGER NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(customerTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void insert(String name, int level) {
        String sql = "INSERT INTO customers(name,level) VALUES(?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, level);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    Customer getCustomer(int id) throws RuntimeException {
        String sql = "SELECT id, name, level "
                + "FROM customers WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
