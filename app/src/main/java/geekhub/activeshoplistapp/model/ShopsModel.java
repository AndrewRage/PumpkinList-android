package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class ShopsModel {
    private int shopId;
    private String shopName;
    private String shopDescription;
    private double gpsLatitude;
    private double gpsLongitude;

    public ShopsModel(int shopId, String shopName) {
        this.shopId = shopId;
        this.shopName = shopName;
    }

    //Setter
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public void setGpsLatitude(double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    //Getter
    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public double getGpsLatitude() {
        return gpsLatitude;
    }

    public double getGpsLongitude() {
        return gpsLongitude;
    }
}
