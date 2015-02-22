package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseListAdapter extends ArrayAdapter<PurchaseListModel> {
    private Context context;
    private int resource;
    private List<PurchaseListModel> purchaseLists;
    private LayoutInflater inflater;

    public PurchaseListAdapter(Context context, int resource, List<PurchaseListModel> purchaseItems) {
        super(context, resource, purchaseItems);
        this.context = context;
        this.resource = resource;
        this.purchaseLists = purchaseItems;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseListModel purchaseList = purchaseLists.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(purchaseList.getListName());
        return convertView;
    }

    private class Holder {
        TextView title;
    }
}