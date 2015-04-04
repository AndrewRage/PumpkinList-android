package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;

/**
 * Created by rage on 3/12/15.
 */
public class DrawerMenuAdapter extends BaseAdapter {
    private static final String TAG = DrawerMenuAdapter.class.getSimpleName();
    private Context context;
    private List<Integer> menuList;
    private int resource;
    private LayoutInflater inflater;

    public DrawerMenuAdapter(Context context, int resource, List<Integer> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Integer getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuList.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(getMenuTitle(menuList.get(position)));
        return convertView;
    }

    private class Holder {
        TextView title;
    }

    private String getMenuTitle(int menuId){
        switch (menuId) {
            case AppConstants.MENU_LOGOUT:
                return context.getString(R.string.menu_logout);
            case AppConstants.MENU_SHOW_PURCHASE_LIST:
                return context.getString(R.string.menu_show_purchase_list);
            case AppConstants.MENU_SHOW_PURCHASE_ARCHIVE:
                return context.getString(R.string.menu_show_purchase_archive);
            case AppConstants.MENU_SHOW_PURCHASE_INBOX:
                return context.getString(R.string.menu_show_purchase_inbox);
            case AppConstants.MENU_SHOW_PURCHASE_OUTBOX:
                return context.getString(R.string.menu_show_purchase_outbox);
            case AppConstants.MENU_SHOW_SHOPS:
                return context.getString(R.string.menu_show_shops);
            case AppConstants.MENU_SHOW_PLACES:
                return context.getString(R.string.menu_show_places);
            case AppConstants.MENU_SHOW_SETTINGS:
                return context.getString(R.string.menu_show_settings);
            default:
                return "";
        }
    }
}
