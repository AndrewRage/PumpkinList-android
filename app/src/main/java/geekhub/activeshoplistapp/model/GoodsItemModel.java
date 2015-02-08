package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class GoodsItemModel {
    private int goodsId;
    private String goodLabel;
    private byte goodMeasure;

    public GoodsItemModel(int goodId, String goodLabel, byte goodMeasure) {
        this.goodsId = goodId;
        this.goodLabel = goodLabel;
        this.goodMeasure = goodMeasure;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public String getGoodLabel() {
        return goodLabel;
    }

    public byte getGoodMeasure() {
        return goodMeasure;
    }

    public void setGoodLabel(String goodLabel) {
        this.goodLabel = goodLabel;
    }

    public void setGoodMeasure(byte goodMeasure) {
        this.goodMeasure = goodMeasure;
    }
}
