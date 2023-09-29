package controller;
import database.Database;
import utils.*;
import objects.*;

import java.sql.SQLException;
import java.util.*;


public class Controller {
    public String run(String command,String data) throws SQLException, ClassNotFoundException, InterruptedException {


        switch (command){
            case "delay notice":
                return delayNotice(data);
            case "asign order":
                return asignOrder(data);
            case "query":
                return Query(data) ? "yes" : "no";

        }
        return "error";
    }

    private boolean Query(String data) throws SQLException, ClassNotFoundException {
        Database db=Database.getInstance();
        Map<String,Integer> map = db.delays();
        List<Integer> sort = map.values().stream().sorted(Comparator.comparingInt(a -> a)).toList();
        sort.forEach(System.out::println);
        return true;

    }

    private String asignOrder(String data) throws SQLException, ClassNotFoundException, InterruptedException {
        Database db=Database.getInstance();
        synchronized (db){
            if(!db.exist(data,"agentsInProgress")){
                return db.asignOrder(data);
            }
            else{
                return "this agent is in progress";
            }
        }
    }

    private String delayNotice(String data) throws SQLException, ClassNotFoundException {
        System.out.println("in delay notice");
        Database db=Database.getInstance();
        synchronized (db) {
            Order order = Convertor.dataToOrder(data);
            return db.addNotice(order.getOrderID(), order.getVendor());
        }
    }


}
