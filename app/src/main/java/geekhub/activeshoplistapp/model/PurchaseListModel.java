package geekhub.activeshoplistapp.model;

import android.location.Location;

import java.util.List;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseListModel {

    public static final int PLACE_SHOP = 1;
    public static final int PLACE_PLACE = 2;
    public static final int PLACE_POINT = 3;

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

    private long alarmTimeStamp;

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

    private Location latencyShopLocation;
    private Location latencyPlaceLocation;
    private Location latencyPointLocation;

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

    public Location getLatencyShopLocation() {
        return latencyShopLocation;
    }

    public void setLatencyShopLocation(Location latencyShopLocation) {
        this.latencyShopLocation = latencyShopLocation;
    }

    public Location getLatencyPlaceLocation() {
        return latencyPlaceLocation;
    }

    public void setLatencyPlaceLocation(Location latencyPlaceLocation) {
        this.latencyPlaceLocation = latencyPlaceLocation;
    }

    public Location getLatencyPointLocation() {
        return latencyPointLocation;
    }

    public void setLatencyPointLocation(Location latencyPointLocation) {
        this.latencyPointLocation = latencyPointLocation;
    }

    public long getAlarmTimeStamp() {
        return alarmTimeStamp;
    }

    public void setAlarmTimeStamp(long alarmTimeStamp) {
        this.alarmTimeStamp = alarmTimeStamp;
    }

    //Index setters and getters
    public Location getLocation(int place) {
        if (place == PLACE_SHOP) {
            return getShopLocation();
        }
        if (place == PLACE_PLACE) {
            return getPlaceLocation();
        }
        if (place == PLACE_POINT) {
            return getPointLocation();
        }
        return null;
    }

    public Location getLatencyLocation(int place) {
        if (place == PLACE_SHOP) {
            return getLatencyShopLocation();
        }
        if (place == PLACE_PLACE) {
            return getLatencyPlaceLocation();
        }
        if (place == PLACE_POINT) {
            return getLatencyPointLocation();
        }
        return null;
    }

    public float getRadius(int place) {
        if (place == PLACE_SHOP) {
            return getShopRadius();
        }
        if (place == PLACE_PLACE) {
            return getPlaceRadius();
        }
        if (place == PLACE_POINT) {
            return getPointRadius();
        }
        return 0;
    }

    public float getMaxDistance(int place) {
        if (place == PLACE_SHOP) {
            return getMaxShopDistance();
        }
        if (place == PLACE_PLACE) {
            return getMaxPlaceDistance();
        }
        if (place == PLACE_POINT) {
            return getMaxPointDistance();
        }
        return 0;
    }

    public void setLocation(int place, Location location) {
        if (place == PLACE_SHOP) {
            setShopLocation(location);
        }
        if (place == PLACE_PLACE) {
            setPlaceLocation(location);
        }
        if (place == PLACE_POINT) {
            setPointLocation(location);
        }
    }

    public void setLatencyLocation(int place, Location location) {
        if (place == PLACE_SHOP) {
            setLatencyShopLocation(location);
        }
        if (place == PLACE_PLACE) {
            setLatencyPlaceLocation(location);
        }
        if (place == PLACE_POINT) {
            setLatencyPointLocation(location);
        }
    }

    public void setRadius(int place, float radius) {
        if (place == PLACE_SHOP) {
            setShopRadius(radius);
        }
        if (place == PLACE_PLACE) {
            setPlaceRadius(radius);
        }
        if (place == PLACE_POINT) {
            setPointRadius(radius);
        }
    }

    public void setMaxDistance(int place, float distance) {
        if (place == PLACE_SHOP) {
            setMaxShopDistance(distance);
        }
        if (place == PLACE_PLACE) {
            setMaxPlaceDistance(distance);
        }
        if (place == PLACE_POINT) {
            setMaxPointDistance(distance);
        }
    }

    public void setMaxDistance(float distance) {
        setMaxShopDistance(distance);
        setMaxPlaceDistance(distance);
        setMaxPointDistance(distance);
    }

    public boolean isSinglePoint(){
        if (getShopLocation() == null && getPlaceLocation() == null) {
            return true;
        } else {
            return false;
        }
    }
}
