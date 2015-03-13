package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseListAdapter extends BaseAdapter {
    private static final String TAG = PurchaseListAdapter.class.getSimpleName();
    private Context context;
    private int resource;
    private List<PurchaseListModel> purchaseLists;
    private LayoutInflater inflater;

    public PurchaseListAdapter(Context context, int resource, List<PurchaseListModel> purchaseLists) {
        this.context = context;
        this.resource = resource;
        this.purchaseLists = purchaseLists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return purchaseLists.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return purchaseLists.get(position).getServerId();
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