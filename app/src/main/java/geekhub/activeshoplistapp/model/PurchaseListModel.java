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
    private boolean isUserShop;
    private long placeId;
    private boolean isUserPlace;
    private boolean isDone;
    private boolean isAlarm;
    private long timeAlarm;
    private long timeCreate;
    private long timeStamp;
    private List<PurchaseItemModel> purchasesItems;

    private Location shopLocation;
    private Location placeLocation;
    private Location pointLocation;
    private float shopRadius;
    private float placeRadius;
    private float pointRadius;
    private float maxShopDistance;
    private float maxPlaceDistance;
    private float maxPointDistance;

    public PurchaseListModel() {

    }

    public PurchaseListModel(long dbId,
                             long serverId,
                             String listName,
                             int userId,
                             long shopId,
                             boolean isUserShop,
                             long placeId,
                             boolean isUserPlace,
                             boolean isDone,
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
        this.isUserShop = isUserShop;
        this.placeId = placeId;
        this.isUserPlace = isUserPlace;
        this.isDone = isDone;
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

    public boolean isUserShop() {
        return isUserShop;
    }

    public void setIsUserShop(boolean isUserShop) {
        this.isUserShop = isUserShop;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public boolean isUserPlace() {
        return isUserPlace;
    }

    public void setIsUserPlace(boolean isUserPlace) {
        this.isUserPlace = isUserPlace;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
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

    public Location getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }

    public Location getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(Location placeLocation) {
        this.placeLocation = placeLocation;
    }

    public Location getPointLocation() {
        return pointLocation;
    }

    public void setPointLocation(Location pointLocation) {
        this.pointLocation = pointLocation;
    }

    public float getShopRadius() {
        return shopRadius;
    }

    public void setShopRadius(float shopRadius) {
        this.shopRadius = shopRadius;
    }

    public float getPlaceRadius() {
        return placeRadius;
    }

    public void setPlaceRadius(float placeRadius) {
        this.placeRadius = placeRadius;
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
    }

    public float getMaxShopDistance() {
        return maxShopDistance;
    }

    public void setMaxShopDistance(float maxShopDistance) {
        this.maxShopDistance = maxShopDistance;
    }

    public float getMaxPlaceDistance() {
        return maxPlaceDistance;
    }

    public void setMaxPlaceDistance(float maxPlaceDistance) {
        this.maxPlaceDistance = maxPlaceDistance;
    }

    public float getMaxPointDistance() {
        return maxPointDistance;
    }

    public void setMaxPointDistance(float maxPointDistance) {
        this.maxPointDistance = maxPointDistance;
    }
}
