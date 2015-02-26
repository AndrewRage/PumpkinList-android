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
        purchasesItem.put(1, new PurchaseItemModel(false, false, false, 1, 1, "Desc_str1", 123));
        purchasesItem.put(2, new PurchaseItemModel(false, false, false, 2, 1, "Desc_str2", 123));
        purchasesItem.put(3, new PurchaseItemModel(false, false, false, 3, 1, "Desc_str3", 123));
        purchasesItem.put(4, new PurchaseItemModel(false, false, false, 4, 1, "Desc_str4", 123));
        purchasesItem.put(5, new PurchaseItemModel(false, false, false, 5, 1, "Desc_str5", 123));
        purchasesItem.put(6, new PurchaseItemModel(false, false, false, 6, 1, "Desc_str6", 123));
        purchasesItem.put(7, new PurchaseItemModel(false, false, false, 7, 1, "Desc_str7", 123));
        purchasesItem.put(8, new PurchaseItemModel(false, false, false, 8, 1, "Desc_str8", 123));
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
