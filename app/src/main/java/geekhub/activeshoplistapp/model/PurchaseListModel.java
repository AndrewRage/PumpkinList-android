package geekhub.activeshoplistapp.model;

import java.util.List;
import java.util.Map;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseListModel {
    private long listId;
    private String listName;
    private int userId;
    private int shopId;
    private long timeAlarm;
    private long timeCreate;
    private long timeStamp;
    private Map<Integer,PurchaseItemModel> purchasesItem;

    public PurchaseListModel(long listId,
                             String listName,
                             int userId,
                             int shopId,
                             long timeAlarm,
                             long timeCreate,
                             long timeStamp,
                             Map<Integer,PurchaseItemModel> purchasesItem) {
        this.listId = listId;
        this.listName = listName;
        this.userId = userId;
        this.shopId = shopId;
        this.timeAlarm = timeAlarm;
        this.timeCreate = timeCreate;
        this.timeStamp = timeStamp;
        this.purchasesItem = purchasesItem;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setTimeAlarm(long timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public void setPurchasesItem(Map<Integer,PurchaseItemModel> purchasesItem) {
        this.purchasesItem = purchasesItem;
    }

    public long getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public int getUserId() {
        return userId;
    }

    public int getShopId() {
        return shopId;
    }

    public long getTimeAlarm() {
        return timeAlarm;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Map<Integer,PurchaseItemModel> getPurchasesItem() {
        return purchasesItem;
    }
}
