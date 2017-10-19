package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.palarz.mike.bakingapp.R;

/**
 * Created by mpala on 10/18/2017.
 */

public class Slider {

    public static void slideDown(Context context, View view){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        if (animation != null){
            animation.reset();
            if (view != null){
                view.clearAnimation();
                view.setAnimation(animation);
            }
        }
    }

    public static void slideUp(Context context, View view){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);

        if (animation != null){
            animation.reset();
            if (view != null){
                view.clearAnimation();
                view.setAnimation(animation);
            }
        }
    }

}
