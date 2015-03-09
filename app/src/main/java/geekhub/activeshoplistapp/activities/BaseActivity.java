package geekhub.activeshoplistapp.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import geekhub.activeshoplistapp.R;

/**
 * Created by rage on 06.02.15.
 * 
 * Base activity
 */
public abstract class BaseActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private boolean drawerHamburgerScreen = false;

    public void initDrawer() {
        String[] menus = getResources().getStringArray(R.array.drawer_menu);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menus);
        setContentView(R.layout.activity_with_fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

        };
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(arrayAdapter);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        drawerHamburgerScreen = true;
        return drawerToggle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                if(drawerHamburgerScreen && drawerLayout != null && backStackEntryCount <= 0){
                    if (drawerLayout.isDrawerOpen(drawerListView)) {
                        drawerLayout.closeDrawer(drawerListView);
                    } else {
                        drawerLayout.openDrawer(drawerListView);
                    }
                } else {
                    onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(drawerListView)) {
            drawerLayout.closeDrawer(drawerListView);
        } else if (onBackPressedListener != null) {
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
