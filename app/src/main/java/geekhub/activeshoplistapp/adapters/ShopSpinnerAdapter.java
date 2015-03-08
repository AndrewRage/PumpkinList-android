package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.model.ShopsModel;

/**
 * Created by rage on 3/8/15.
 */
public class ShopSpinnerAdapter extends ArrayAdapter<ShopsModel> {
    private List<ShopsModel> shopsList;
    private int resource;
    private LayoutInflater inflater;

    public ShopSpinnerAdapter(Context context, int resource, List<ShopsModel> shopsList) {
        super(context, resource, shopsList);
        this.shopsList = shopsList;
        this.resource = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ShopsModel shop = shopsList.get(position);
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
