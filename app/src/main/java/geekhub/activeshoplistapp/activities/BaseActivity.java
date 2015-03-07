package geekhub.activeshoplistapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by rage on 06.02.15.
 * 
 * Base activity
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            if (onBackPressedListener.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
                super.onBackPressed();
        }
    }

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public interface OnBackPressedListener {
        public boolean onBackPressed();
    }
}
