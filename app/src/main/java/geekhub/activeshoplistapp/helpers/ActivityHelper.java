package geekhub.activeshoplistapp.helpers;

/**
 * Created by rage on 4/4/15.
 */
public class ActivityHelper {
    private static ActivityHelper helper;
    private int menuId = AppConstants.MENU_SHOW_PURCHASE_LIST;
    private int globalId = AppConstants.MENU_SHOW_PURCHASE_LIST;

    private ActivityHelper() {
    }

    public static ActivityHelper getInstance(){
        if (helper == null) {
            helper = new ActivityHelper();
        }
        return helper;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getGlobalId() {
        return globalId;
    }

    public void setGlobalId(int globalId) {
        this.globalId = globalId;
    }
}
