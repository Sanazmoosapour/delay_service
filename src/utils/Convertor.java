package utils;
import objects.*;

public class Convertor {
    public static Order dataToOrder(String data){
        String[] splited = data.split(",");
        Order order = new Order();

        order.setOrderID(splited[0]);
        order.setVendor(splited[1]);
        order.setDeliveryTime(Integer.parseInt(splited[2]));
        order.setState(TripState.valueOf(splited[3]));

        return order;
    }
}
