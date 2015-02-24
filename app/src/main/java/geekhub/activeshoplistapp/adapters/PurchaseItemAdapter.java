package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PurchaseItemModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseItemAdapter extends ArrayAdapter<PurchaseItemModel> {
    private Context context;
    private int resource;
    private List<PurchaseItemModel> purchaseItemLists;
    private LayoutInflater inflater;

    public PurchaseItemAdapter(Context context, int resource, List<PurchaseItemModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.purchaseItemLists = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseItemModel purchaseList = purchaseItemLists.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(purchaseList.getGoodsId() + purchaseList.getGoodsDescription());
        return convertView;
    }

    private class Holder {
        TextView title;
    }
}
