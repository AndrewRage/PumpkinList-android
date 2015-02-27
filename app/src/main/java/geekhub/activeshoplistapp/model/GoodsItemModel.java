package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 08.02.15. Create by task: 003
 */
public class GoodsItemModel {
    private int goodsId;
    private boolean isUserGoods;
    private String goodLabel;
    private int goodCategoryId;
    private int goodMeasureId;
    private String goodsDescription;

    public GoodsItemModel(int goodsId, boolean isUserGoods, String goodLabel, int goodCategoryId, int goodMeasureId, String goodsDescription) {
        this.goodsId = goodsId;
        this.isUserGoods = isUserGoods;
        this.goodLabel = goodLabel;
        this.goodCategoryId = goodCategoryId;
        this.goodMeasureId = goodMeasureId;
        this.goodsDescription = goodsDescription;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public boolean isUserGoods() {
        return isUserGoods;
    }

    public String getGoodLabel() {
        return goodLabel;
    }

    public int getGoodCategoryId() {
        return goodCategoryId;
    }

    public int getGoodMeasureId() {
        return goodMeasureId;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }
}
