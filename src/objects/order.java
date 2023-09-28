package objects;

public class order {
    private String orderID;
    private String vendor;
    private int deliveryTime;
    private TripState state;

    public order() {

    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public TripState getState() {
        return state;
    }

    public void setState(TripState state) {
        this.state = state;
    }
}
