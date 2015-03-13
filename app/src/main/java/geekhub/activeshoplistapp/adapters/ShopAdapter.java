package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.PlacesModel;

/**
 * Created by rage on 3/4/15.
 */
public class ShopAdapter extends BaseAdapter {
    private static final String TAG = ShopAdapter.class.getSimpleName();
    private Context context;
    private int resource;
    private List<PlacesModel> shopLists;
    private LayoutInflater inflater;

    public ShopAdapter(Context context, int resource, List<PlacesModel> shopLists) {
        this.context = context;
        this.resource = resource;
        this.shopLists = shopLists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shopLists.size();
    }

    @Override
    public PlacesModel getItem(int position) {
        return shopLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shopLists.get(position).getServerId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacesModel shop = shopLists.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(shop.getShopName());
        return convertView;
    }

    private class Holder {
        TextView title;
    }
}
