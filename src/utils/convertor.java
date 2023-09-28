package utils;
import objects.*;

public class convertor {
    public static order dataToOrder(String data){
        String[] splited=data.split(",");
        order order=new order();
        order.setOrderID(splited[0]);
        order.setVendor(splited[1]);
        order.setDeliveryTime(Integer.parseInt(splited[2]));
        order.setState(TripState.valueOf(splited[3]));
        return order;
    }
}
