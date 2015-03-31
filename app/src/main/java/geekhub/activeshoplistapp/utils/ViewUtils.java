package geekhub.activeshoplistapp.utils;

import android.os.Build;
import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by rage on 3/31/15.
 */
public class ViewUtils {

    public static void setY(View view, float y){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            TranslateAnimation anim=new TranslateAnimation(0, 0, y, y);
            anim.setFillAfter(true);
            anim.setDuration(0);
            view.startAnimation(anim);
        } else {
            view.setY(y);
        }
    }

    public static void setX(View view, float x){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            TranslateAnimation anim=new TranslateAnimation(x, x, 0, 0);
            anim.setFillAfter(true);
            anim.setDuration(0);
            view.startAnimation(anim);
        } else {
            view.setX(x);
        }
    }

}
