package geekhub.activeshoplistapp.asyncs;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.utils.AlarmUtils;

/**
 * Created by rage on 5/11/15.
 */
public class AddNewListTask extends AsyncTask<Void, Void, Void> {
    private PurchaseListModel purchaseList;
    private Context context;

    public AddNewListTask(Context context, PurchaseListModel purchaseList) {
        this.purchaseList = purchaseList;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Uri uri = ContentHelper.insertPurchaseList(context, purchaseList);
        purchaseList.setDbId(Long.parseLong(uri.getLastPathSegment()));

        if (purchaseList.getTimeAlarm() > System.currentTimeMillis()
                && purchaseList.getDbId() > 0) {
            AlarmUtils alarmUtils = new AlarmUtils(context);
            alarmUtils.setListAlarm(purchaseList);
        }
        return null;
    }
}
