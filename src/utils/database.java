package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.*;
import network.*;

public class database {
    Connection connection;
    static database DB=null;
    private database() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/delay_service",
                "root", "401243090sanazMoosa");
    }

    public static database getInstance() throws SQLException, ClassNotFoundException {
        if(DB ==null){
            return new database();

        }
        return DB;
    }
    public boolean exist(String ID,String tableName) throws SQLException, ClassNotFoundException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery(
                "select * from "+tableName);
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
        resultSet = statement.executeQuery(
                "select * from full_data");

        while (resultSet.next()) {
            if(resultSet.getString("order_id").equals(ID) && resultSet.getString("vendor").equals(vendor)){
                if(!resultSet.getString("trip_state").equals("DELIVERED")){
                    int delayTime=9;//////////////////////////////////////////////////////
                    if(!exist(ID,"delay_reports")) {
                        addRowToReports(ID, vendor, delayTime);
                    }
                    return "delay time is ----";
                }
            }
        }
        if(! exist(ID,"delay_queue")){
            addRowToQueue(ID,vendor);
            return "your request is in delay queue";
        }
        return "false";
    }
    public boolean addRowToReports(String orderID,String Vendor,int delayTime) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        String accessDatabase = "insert into delay_reports (order_id,delay_time)" + " values("+orderID+","+delayTime+") ";
        int result = statement.executeUpdate(accessDatabase);
        statement.close();
        if (result > 0) {
            return true;
        }
        return false;
    }
    public boolean addRowToQueue(String orderID,String Vendor) throws SQLException {
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

        String accessDatabase = "insert into working_agents(agent_id)" + " values("+agentID+") ";
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
    public String asignOrder(String agentID) throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery(
                "select * from delay_queue");

        while (resultSet.next()) {
            if(!exist(resultSet.getString("order_id"),"delays_in_progress")){
                addRowToAgents(agentID);
                addRowToDelays(resultSet.getString("order_id"));
                DataInputStream dis=new DataInputStream(requestHandler.socket.getInputStream());
                DataOutputStream dos=new DataOutputStream(requestHandler.socket.getOutputStream());
                dos.writeUTF("write the delay time of order : "+resultSet.getString("order_id"));
                dos.flush();
                int delay_time=Integer.parseInt(dis.readUTF());

            }

        }
        return "request failed";
    }
    public Map<String,Integer> delays() throws SQLException, ClassNotFoundException {
        Map<String,Integer> map=new HashMap<>();
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery(
                "select * from delay_reports");
        while (resultSet.next()) {
            if(!map.containsKey(resultSet.getString("vendor"))){
                map.put(resultSet.getString("vendor"),resultSet.getInt("delay_time"));
            }
            else
                map.put(resultSet.getString("vendor"),resultSet.getInt("delay_time")+map.get(resultSet.getString("vendor")));
        }
        return map;
    }
}
