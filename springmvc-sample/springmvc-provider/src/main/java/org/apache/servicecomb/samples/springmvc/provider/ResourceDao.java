package org.apache.servicecomb.samples.springmvc.provider;

import java.sql.*;
import java.util.ArrayList;

public class ResourceDao extends Dao {
    ResourceDao(String fileName) {
        super(fileName);
        String resourceTableSql = "CREATE TABLE IF NOT EXISTS resources ("
                + "	id integer PRIMARY KEY,"
                + "	state integer NOT NULL,"
                + "	customer_id integer"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(resourceTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void insert() {
        String sql = "INSERT INTO resources(state) VALUES(0)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Resource getResource(int id) throws RuntimeException {
        String sql = "SELECT id, state, customer_id "
                + "FROM resources WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Resource(rs.getInt("id"),
                        rs.getInt("state"),
                        rs.getInt("customer_id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Db error");
        }
    }

    ArrayList<Resource> getUsedResourcesByCustomerId(int id) throws RuntimeException {
        String sql = "SELECT id, state, customer_id "
                + "FROM resources WHERE customer_id = ? AND state = 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Resource> resources = new ArrayList<>();
            while (rs.next()) {
                Resource resource =
                        new Resource(rs.getInt("id"),
                                rs.getInt("state"),
                                rs.getInt("customer_id"));
                resources.add(resource);
            }
            return resources;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Db error");
        }
    }

    private void changeResourceState(int resourceId, int state, int customerId) throws RuntimeException {

    }


    ArrayList<Resource> getAvailableResources() throws RuntimeException {
        String sql = "SELECT id, state, customer_id "
                + "FROM resources WHERE state = 0";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Resource> resources = new ArrayList<>();
            while (rs.next()) {
                resources.add(new Resource(rs.getInt("id"),
                        rs.getInt("state"),
                        rs.getInt("customer_id")));
            }
            return resources;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Db error");
        }
    }

    void releaseResource(int resourceId, int customerId) {
        String sql = "UPDATE resources SET state = 0 WHERE id = ? AND customer_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, resourceId);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Db error");
        }
    }

    void acquireResource(int resourceId, int customerId) {
        String sql = "UPDATE resources SET state = 1, customer_id = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, resourceId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Db error");
        }
    }
}
