package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PurchaseItemModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseItemAdapter extends BaseAdapter {
    private static final String TAG = PurchaseItemAdapter.class.getSimpleName();
    private Context context;
    private int resource;
    private List<PurchaseItemModel> purchaseItems;
    private LayoutInflater inflater;

    public PurchaseItemAdapter(Context context, int resource, List<PurchaseItemModel> objects) {
        this.context = context;
        this.resource = resource;
        this.purchaseItems = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return purchaseItems == null ? 0 : purchaseItems.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return purchaseItems.get(position).getServerId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseItemModel purchaseItem = purchaseItems.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_bought);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(purchaseItem.getGoodsLabel());
        holder.checkBox.setChecked(purchaseItem.isBought());
        return convertView;
    }

    private class Holder {
        TextView title;
        CheckBox checkBox;
    }
}
