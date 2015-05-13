package geekhub.activeshoplistapp.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 5/13/15.
 */
public class UpdateListTask extends AsyncTask<Void, Void, Void> {
    private PurchaseListModel purchaseList;
    private Context context;

    public UpdateListTask(PurchaseListModel purchaseList, Context context) {
        this.purchaseList = purchaseList;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentHelper.updatePurchaseList(context, purchaseList);
        return null;
    }
}
