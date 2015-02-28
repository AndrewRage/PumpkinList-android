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

        //===========
        //Fake data!!!
        /*purchaseLists = new ArrayList<>();
        Map<Integer,PurchaseItemModel> purchasesItem = new TreeMap<>();
        purchasesItem.put(1, new PurchaseItemModel(false, false, 1, "", 1, "Desc_str1", 123));
        purchaseLists.add( new PurchaseListModel(1, 1,"List1", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.add( new PurchaseListModel(2, 2,"List2", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.add( new PurchaseListModel(3, 3,"List3", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.add( new PurchaseListModel(4, 4,"List4", 1, 1, 0, 0, 0, purchasesItem));*/
        //===========
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
            dataBaseHelper.close();
        }
        return purchaseLists;
    }

    public long addPurchaseList(PurchaseListModel purchaseList) {
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPurchaseList(purchaseList);
        dataBaseHelper.close();
        purchaseList.setDbId(rawId);
        purchaseLists.add(0, purchaseList);
        return rawId;
    }

    public void deletePurchaseList(PurchaseListModel purchaseList) {
        dataBaseHelper.open();
        dataBaseHelper.deletePurchaseList(purchaseList.getDbId());
        dataBaseHelper.close();
        purchaseLists.remove(purchaseList);
    }
}
