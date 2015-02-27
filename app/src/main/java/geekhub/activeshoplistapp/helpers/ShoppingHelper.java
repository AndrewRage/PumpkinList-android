package geekhub.activeshoplistapp.helpers;

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
    private Map<Long,PurchaseListModel> purchaseLists;

    private ShoppingHelper() {
        //===========
        //Fake data!!!
        purchaseLists = new TreeMap<Long,PurchaseListModel>();
        Map<Integer,PurchaseItemModel> purchasesItem = new TreeMap<>();
        purchasesItem.put(1, new PurchaseItemModel(false, false, 1, "", 1, "Desc_str1", 123));
        purchaseLists.put((long) 10, new PurchaseListModel("List1", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.put((long) 20, new PurchaseListModel("List2", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.put((long) 30, new PurchaseListModel("List3", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.put((long) 40, new PurchaseListModel("List4", 1, 1, 0, 0, 0, purchasesItem));
        purchaseLists.put((long) 50, new PurchaseListModel("List5", 1, 1, 0, 0, 0, purchasesItem));
        //===========
    }

    public static ShoppingHelper getInstance() {
        if (shoppingHelper == null) {
            shoppingHelper = new ShoppingHelper();
        }
        return shoppingHelper;
    }

    public Map<Long, PurchaseListModel> getPurchaseLists() {
        return purchaseLists;
    }
}
