package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class PurchaseItemModel {
    private boolean isUserGood; //if it a user goods id
    private boolean isBought; //true if bye
    private boolean isCancel; //if user decided not to buy goods -- for synchronize
    private int goodsId;
    private float goodsQuantity;
    private String goodsDescription;
    private long timeStamp;

    public PurchaseItemModel(boolean isUserGood,
                             boolean isBought,
                             boolean isCancel,
                             int goodsId,
                             float goodsQuantity,
                             String goodsDescription,
                             long timeStamp) {
        this.isUserGood = isUserGood;
        this.isBought = isBought;
        this.isCancel = isCancel;
        this.goodsId = goodsId;
        this.goodsQuantity = goodsQuantity;
        this.goodsDescription = goodsDescription;
        this.timeStamp = timeStamp;
    }

    //Setter
    public void setUserGood(boolean isUserGood) {
        this.isUserGood = isUserGood;
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
    public boolean isUserGood() {
        return isUserGood;
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
