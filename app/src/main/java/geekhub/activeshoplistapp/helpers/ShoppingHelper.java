package geekhub.activeshoplistapp.helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 2/24/15.
 */
public class ShoppingHelper {
    private static ShoppingHelper shoppingHelper;
    private List<PurchaseListModel> purchaseLists;
    private DataBaseHelper dataBaseHelper;

    private ShoppingHelper(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }
    public static ShoppingHelper newInstance(Context context) {
        if (shoppingHelper == null) {
            shoppingHelper = new ShoppingHelper(context);
        }
        return shoppingHelper;
    }

    public static ShoppingHelper getInstance() {
        return shoppingHelper;
    }

    public List<PurchaseListModel> getPurchaseLists() {
        if (purchaseLists == null) {
            dataBaseHelper.open();
            purchaseLists = dataBaseHelper.getPurchaseLists();
            for (PurchaseListModel list: purchaseLists) {
                list.setPurchasesItems(dataBaseHelper.getPurchaseItems(list.getDbId()));
            }
            dataBaseHelper.close();
        }
        return purchaseLists;
    }

    public long addPurchaseList(PurchaseListModel purchaseList) {
        purchaseList.setTimeCreate(System.currentTimeMillis());
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPurchaseList(purchaseList);
        dataBaseHelper.close();
        for (PurchaseItemModel item: purchaseList.getPurchasesItems()) {
            addPurchaseItem(item, rawId);
        }
        purchaseList.setDbId(rawId);
        purchaseLists.add(0, purchaseList);
        return rawId;
    }

    public void deletePurchaseList(PurchaseListModel purchaseList) {
        dataBaseHelper.open();
        dataBaseHelper.deletePurchaseList(purchaseList.getDbId());
        dataBaseHelper.deletePurchaseItem(purchaseList.getDbId());
        dataBaseHelper.close();
        purchaseLists.remove(purchaseList);
    }

    public void updatePurchaseList(PurchaseListModel purchaseList) {
        purchaseList.setTimeStamp(0);
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseList(purchaseList);
        dataBaseHelper.close();
    }

    public long addPurchaseItem(PurchaseItemModel purchaseItem, long listId) {
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPurchaseItem(purchaseItem, listId);
        dataBaseHelper.close();
        purchaseItem.setDbId(rawId);
        return rawId;
    }

    public void updatePurchaseItem(PurchaseItemModel item) {
        item.setTimeStamp(0);
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseItem(item);
        dataBaseHelper.close();
    }
}
