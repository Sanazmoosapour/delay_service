package controller;
import database.Database;
import utils.*;
import objects.*;

import java.sql.SQLException;
import java.util.*;


public class Controller {
    public String run(String command,String data)
            throws SQLException,
            ClassNotFoundException,
            InterruptedException {


        switch (command){
            case "delay notice":
                return delayNotice(data);
            case "asign order":
                return asignOrder(data);
            case "query":
                return Query() ? " " : "query error";
        }
        return "error";
    }

    private boolean Query() throws SQLException, ClassNotFoundException {
        Database db = Database.getInstance();
        Map<String,Integer> map = db.delays();
        List<Map.Entry<String, Integer> > list = new LinkedList<>(map.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> aa : list) {
            System.out.println(aa.getKey() + " : " + aa.getValue());
        }
        return true;

    }

    private String asignOrder(String data) throws SQLException, ClassNotFoundException {
        Database db = Database.getInstance();
        synchronized (db){
            if(! db.exist(data, "working_agents")){
                return db.asignOrder(data);
            }
            else{
                return "this agent is in progress";
            }
        }
    }

    private String delayNotice(String data) throws SQLException, ClassNotFoundException {
        Database db = Database.getInstance();
        synchronized (db) {
            Order order = Convertor.dataToOrder(data);
            return db.addNotice(order.getOrderID(), order.getVendor());
        }
    }


}
