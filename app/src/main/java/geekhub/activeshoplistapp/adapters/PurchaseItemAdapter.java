package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseItemModel;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseItemAdapter extends CursorAdapter {
    private static final String TAG = PurchaseItemAdapter.class.getSimpleName();
    private int resource;
    private LayoutInflater inflater;

    public PurchaseItemAdapter(Context context, Cursor c, int resource) {
        super(context, c);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    public PurchaseItemAdapter(Context context, Cursor c, int flags, int resource) {
        super(context, c, flags);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Holder holder = new Holder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.checkBox = (CheckBox) view.findViewById(R.id.check_bought);
        view.setTag(holder);

        populateHolder(cursor, holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = (Holder) view.getTag();

        populateHolder(cursor, holder);
    }

    private class Holder {
        TextView title;
        CheckBox checkBox;
    }

    private void populateHolder(Cursor cursor, Holder holder) {
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
        int indexBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);

        holder.title.setText(cursor.getString(indexName));
        holder.checkBox.setChecked(cursor.getInt(indexBought) > 0);
    }
}
