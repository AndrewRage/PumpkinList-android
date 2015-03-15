package geekhub.activeshoplistapp.model;

import android.location.Location;

import java.util.List;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseListModel {
    private long dbId;
    private long serverId;
    private String listName;
    private int userId;
    private long shopId;
    private long placeId;
    private boolean isDone;
    private float maxDistance;
    private boolean isAlarm;
    private long timeAlarm;
    private long timeCreate;
    private long timeStamp;
    private List<PurchaseItemModel> purchasesItems;

    private Location point;
    private float radius;

    public PurchaseListModel() {

    }

    public PurchaseListModel(long dbId,
                             long serverId,
                             String listName,
                             int userId,
                             long shopId,
                             long placeId,
                             boolean isDone,
                             float maxDistance,
                             boolean isAlarm,
                             long timeAlarm,
                             long timeCreate,
                             long timeStamp,
                             List<PurchaseItemModel> purchasesItems) {
        this.dbId = dbId;
        this.serverId = serverId;
        this.listName = listName;
        this.userId = userId;
        this.shopId = shopId;
        this.placeId = placeId;
        this.isDone = isDone;
        this.maxDistance = maxDistance;
        this.isAlarm = isAlarm;
        this.timeAlarm = timeAlarm;
        this.timeCreate = timeCreate;
        this.timeStamp = timeStamp;
        this.purchasesItems = purchasesItems;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

    public long getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(long timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<PurchaseItemModel> getPurchasesItems() {
        return purchasesItems;
    }

    public void setPurchasesItems(List<PurchaseItemModel> purchasesItems) {
        this.purchasesItems = purchasesItems;
    }

    public Location getPoint() {
        return point;
    }

    public void setPoint(Location point) {
        this.point = point;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
