package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseItemModel {
    private long dbId;
    private long serverId;
    private long listDbId;
    private boolean isBought; //true if bye
    private boolean isCancel; //if user decided not to buy goods -- for synchronize
    private int goodsId;
    private String goodsLabel;
    private float goodsQuantity;
    private String goodsDescription;
    private long timeStamp;

    public PurchaseItemModel() {

    }

    public PurchaseItemModel(long dbId,
                             long serverId,
                             long listDbId,
                             boolean isBought,
                             boolean isCancel,
                             int goodsId,
                             String goodsLabel,
                             float goodsQuantity,
                             String goodsDescription,
                             long timeStamp) {
        this.dbId = dbId;
        this.serverId = serverId;
        this.listDbId = listDbId;
        this.isBought = isBought;
        this.isCancel = isCancel;
        this.goodsId = goodsId;
        this.goodsLabel = goodsLabel;
        this.goodsQuantity = goodsQuantity;
        this.goodsDescription = goodsDescription;
        this.timeStamp = timeStamp;
    }

    //Setter
    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setListDbId(long listDbId) {
        this.listDbId = listDbId;
    }

    public void setBought(boolean isBought) {
        this.isBought = isBought;
    }

    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setGoodsLabel(String goodsLabel) {
        this.goodsLabel = goodsLabel;
    }

    public void setGoodsQuantity(float goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    //Getter
    public long getDbId() {
        return dbId;
    }

    public long getServerId() {
        return serverId;
    }

    public long getListDbId() {
        return listDbId;
    }

    public boolean isBought() {
        return isBought;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public String getGoodsLabel() {
        return goodsLabel;
    }

    public float getGoodsQuantity() {
        return goodsQuantity;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
