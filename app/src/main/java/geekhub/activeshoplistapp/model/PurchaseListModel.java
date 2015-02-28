package geekhub.activeshoplistapp.model;

import java.util.List;
import java.util.Map;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseListModel {
    private int dbId;
    private long serverId;
    private String listName;
    private int userId;
    private int shopId;
    private long timeAlarm;
    private long timeCreate;
    private long timeStamp;
    private Map<Integer,PurchaseItemModel> purchasesItem;

    public PurchaseListModel() {

    }

    public PurchaseListModel(int dbId,
                             long serverId,
                             String listName,
                             int userId,
                             int shopId,
                             long timeAlarm,
                             long timeCreate,
                             long timeStamp,
                             Map<Integer, PurchaseItemModel> purchasesItem) {
        this.dbId = dbId;
        this.serverId = serverId;
        this.listName = listName;
        this.userId = userId;
        this.shopId = shopId;
        this.timeAlarm = timeAlarm;
        this.timeCreate = timeCreate;
        this.timeStamp = timeStamp;
        this.purchasesItem = purchasesItem;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
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

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public Map<Integer, PurchaseItemModel> getPurchasesItem() {
        return purchasesItem;
    }

    public void setPurchasesItem(Map<Integer, PurchaseItemModel> purchasesItem) {
        this.purchasesItem = purchasesItem;
    }
}
