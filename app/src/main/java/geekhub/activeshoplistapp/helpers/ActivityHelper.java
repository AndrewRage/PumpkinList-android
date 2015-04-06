package geekhub.activeshoplistapp.helpers;

/**
 * Created by rage on 4/4/15.
 */
public class ActivityHelper {
    private static ActivityHelper helper;
    private int purchaseMenuId = AppConstants.MENU_SHOW_PURCHASE_LIST;
    private int globalId = AppConstants.MENU_SHOW_PURCHASE_LIST;
    private boolean isNotMainScreen = false;

    private ActivityHelper() {
    }

    public static ActivityHelper getInstance(){
        if (helper == null) {
            helper = new ActivityHelper();
        }
        return helper;
    }

    public int getPurchaseMenuId() {
        return purchaseMenuId;
    }

    public void setPurchaseMenuId(int purchaseMenuId) {
        this.purchaseMenuId = purchaseMenuId;
    }

    public int getGlobalId() {
        return globalId;
    }

    public void setGlobalId(int globalId) {
        this.globalId = globalId;
    }

    public boolean isNotMainScreen() {
        return isNotMainScreen;
    }

    public void setIsNotMainScreen(boolean isNotMainScreen) {
        this.isNotMainScreen = isNotMainScreen;
    }
}
