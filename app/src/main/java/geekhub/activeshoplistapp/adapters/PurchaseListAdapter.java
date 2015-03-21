package geekhub.activeshoplistapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;

/**
 * Created by rage on 3/21/15.
 */
public class PurchaseListAdapter extends CursorAdapter {
    private int resource;
    private LayoutInflater inflater;

    public PurchaseListAdapter(Context context, Cursor c, int resource) {
        super(context, c);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    public PurchaseListAdapter(Context context, Cursor c, int flags, int resource) {
        super(context, c, flags);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Holder holder = new Holder();
        holder.title = (TextView) view.findViewById(R.id.title);
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
    }

    private void populateHolder(Cursor cursor, Holder holder) {
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);

        holder.title.setText(cursor.getString(indexName));
    }
}
