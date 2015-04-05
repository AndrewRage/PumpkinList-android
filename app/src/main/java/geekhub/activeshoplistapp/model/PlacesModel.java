package geekhub.activeshoplistapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PlacesModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dbId);
        dest.writeLong(this.serverId);
        dest.writeLong(this.category);
        dest.writeString(this.shopName);
        dest.writeString(this.shopDescription);
        dest.writeDouble(this.gpsLatitude);
        dest.writeDouble(this.gpsLongitude);
        dest.writeByte(isDelete ? (byte) 1 : (byte) 0);
        dest.writeLong(this.timeStamp);
    }

    private PlacesModel(Parcel in) {
        this.dbId = in.readLong();
        this.serverId = in.readLong();
        this.category = in.readLong();
        this.shopName = in.readString();
        this.shopDescription = in.readString();
        this.gpsLatitude = in.readDouble();
        this.gpsLongitude = in.readDouble();
        this.isDelete = in.readByte() != 0;
        this.timeStamp = in.readLong();
    }

    public static final Parcelable.Creator<PlacesModel> CREATOR = new Parcelable.Creator<PlacesModel>() {
        public PlacesModel createFromParcel(Parcel source) {
            return new PlacesModel(source);
        }

        public PlacesModel[] newArray(int size) {
            return new PlacesModel[size];
        }
    };
}
