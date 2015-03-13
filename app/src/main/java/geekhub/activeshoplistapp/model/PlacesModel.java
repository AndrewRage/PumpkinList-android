package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PlacesModel {
    private long dbId;
    private long serverId;
    private long category;
    private String shopName;
    private String shopDescription;
    private double gpsLatitude;
    private double gpsLongitude;
    private boolean isDelete;
    private long timeStamp;

    public PlacesModel() {

    }

    public PlacesModel(long dbId,
                       long serverId,
                       long category,
                       String shopName,
                       String shopDescription,
                       double gpsLatitude,
                       double gpsLongitude,
                       boolean isDelete,
                       long timeStamp) {
        this.dbId = dbId;
        this.serverId = serverId;
        this.category = category;
        this.shopName = shopName;
        this.shopDescription = shopDescription;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.isDelete = isDelete;
        this.timeStamp = timeStamp;
    }

    //Setters
    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setCategory(long category) {
        this.category = category;
    }

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

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    //Getters
    public long getDbId() {
        return dbId;
    }

    public long getServerId() {
        return serverId;
    }

    public long getCategory() {
        return category;
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

    public boolean isDelete() {
        return isDelete;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
