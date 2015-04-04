package geekhub.activeshoplistapp.helpers;

/**
 * Created by rage on 4/4/15.
 */
public class PurchaseActivityHelper {
    private static PurchaseActivityHelper helper;
    private int menuId = AppConstants.MENU_SHOW_PURCHASE_LIST;

    private PurchaseActivityHelper() {
    }

    public static PurchaseActivityHelper getInstance(){
        if (helper == null) {
            helper = new PurchaseActivityHelper();
        }
        return helper;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
