package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseListAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private Map<Integer,PurchaseListModel> purchaseLists;
    private LayoutInflater inflater;
    private List<Integer> keys;

    public PurchaseListAdapter(Context context, int resource, Map<Integer,PurchaseListModel> purchaseItems/*, List<Integer> keys*/) {
        //super(context, resource, keys);
        this.context = context;
        this.resource = resource;
        this.purchaseLists = purchaseItems;
        this.keys = new ArrayList<>(purchaseLists.keySet());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseLists.get(keys.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseListModel purchaseList = purchaseLists.get(keys.get(position));
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