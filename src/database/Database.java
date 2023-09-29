package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.*;
import java.util.*;

public class Database {
    Connection connection;
    static Database DB = null;
    private Database() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/delay_service",
                "root", "401243090sanazMoosa");
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(DB == null){
            return new Database();
        }
        return DB;
    }

    public boolean exist(String ID,String tableName) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;

        resultSet = statement.executeQuery("select * from " + tableName);

        while (resultSet.next()) {
            if(resultSet.getString("order_id").equals(ID)){
                return true;
            }
        }
        return false;
    }

    public String addNotice(String ID,String vendor) throws SQLException, ClassNotFoundException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;

        resultSet = statement.executeQuery("select * from full_data");

        while (resultSet.next()) {
            if(resultSet.getString("order_id").equals(ID) && resultSet.getString("vendor").equals(vendor)){
                if(! resultSet.getString("trip_state").equals("DELIVERED")){
                    int delayTime=getDelayTime(ID);
                    if(! exist(ID, "delay_reports")) {
                        addRowToReports(ID, vendor, delayTime);
                    }
                    return "delay time is "+delayTime;
                }
            }
        }
        if(! exist(ID, "delay_queue")){
            addRowToQueue(ID);
            return "your request is in delay queue";
        }
        return "false";
    }
    public boolean addRowToReports(String orderID,String Vendor,int delayTime) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        String accessDatabase = "insert into delay_reports (order_id,delay_time)" + " values("+orderID + "," + delayTime + ") ";
        int result = statement.executeUpdate(accessDatabase);
        statement.close();
        if (result > 0) {
            return true;
        }
        return false;
    }
    public boolean addRowToQueue(String orderID) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        String accessDatabase = "insert into delay_queue(order_id)" + " values("+orderID+") ";
        int result = statement.executeUpdate(accessDatabase);
        statement.close();
        if (result > 0) {
            return true;
        }
        return false;
    }
    public boolean addRowToAgents(String agentID) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        String accessDatabase = "insert into working_agents(agent_id)" + " values(" + agentID + ") ";
        int result = statement.executeUpdate(accessDatabase);
        statement.close();
        if (result > 0) {
            return true;
        }
        return false;
    }
    public boolean addRowToDelays(String orderID) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        String accessDatabase = "insert into delays_in_progress(order_id)" + " values("+orderID+") ";
        int result = statement.executeUpdate(accessDatabase);
        statement.close();
        if (result > 0) {
            return true;
        }
        return false;
    }
    public String asignOrder(String agentID) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("select * from delay_queue");

        while (resultSet.next()) {
            if(! exist(resultSet.getString("order_id"), "delays_in_progress")){
                addRowToAgents(agentID);
                addRowToDelays(resultSet.getString("order_id"));
                return "delay order is in progress";
            }

        }
        return "request failed";
    }
    public Map<String,Integer> delays() throws SQLException {
        Map<String,Integer> map = new HashMap<>();
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("select * from delay_reports");
        while (resultSet.next()) {
            if(! map.containsKey(resultSet.getString("vendor"))){
                map.put(resultSet.getString("vendor"), resultSet.getInt("delay_time"));
            }
            else
                map.put(resultSet.getString("vendor"), resultSet.getInt("delay_time") + map.get(resultSet.getString("vendor")));
        }
        return map;
    }
    public int getDelayTime(String id) throws SQLException {
        String baseUrl = "https://run.mocky.io/v3/122c2796-5df4-461c-ab75-87c1192b17f7" + "?id=" + id;

        try {

            URL url = new URL(baseUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("eta")){
                    in.close();
                    connection.disconnect();
                    return Integer.parseInt(inputLine.split(":")[1]);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
