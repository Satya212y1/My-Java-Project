package com.zenscale.zencrm_2.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class db {


    private final String url;

    private final String user;

    private final String pass;




    public db(String url, String user, String pass) {

        this.url = url;
        this.user = user;
        this.pass = pass;
    }




    public void execute(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.executeUpdate(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }




    public double get_single_double(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            double o = 0;

            while (rs.next()) {
                o = rs.getDouble(1);

            }

            return o;
        } catch (SQLException e) {
            // handle the exceptio

            return -1;

        }
    }




    public boolean isExist(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            int o = 0;
            while (rs.next()) {
                o = rs.getInt(1);
            }
            return o > 0;
        } catch (SQLException e) {
            return false;
        }

    }




    public int get_single_integer(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            int o = 0;

            while (rs.next()) {
                o = rs.getInt(1);

            }

            return o;
        } catch (SQLException e) {
            // handle the exceptio

            return -1;

        }
    }




    public String get_single(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            String o = "";

            while (rs.next()) {
                o = rs.getString(1);

            }

            return o;
        } catch (SQLException e) {
            // handle the exceptio

            return null;

        }
    }




    public List<Map<String, Object>> get(String query) {

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            Map<String, Object> row = null;
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while (rs.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {

                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                resultList.add(row);
            }
            return resultList;
        } catch (SQLException e) {
            // handle the exceptio

            return null;

        }
    }




    public List<Map<String, Object>> executeQuery(String q) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, pass);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(q);
        while (rs.next()) {
            row = new HashMap<String, Object>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
            }
            resultList.add(row);
        }
        return resultList;
    }




}
