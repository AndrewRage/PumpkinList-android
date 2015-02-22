package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import geekhub.activeshoplistapp.model.PurchaseItemModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseItemAdapter extends ArrayAdapter<PurchaseItemModel> {
    public PurchaseItemAdapter(Context context, int resource, List<PurchaseItemModel> objects) {
        super(context, resource, objects);
    }
}
