package com.tmaprojects.tarekkma.tedxainshamstickets.Utils;

import android.graphics.Color;
import android.support.annotation.ColorRes;

import com.tmaprojects.tarekkma.tedxainshamstickets.R;

/**
 * Created by tarekkma on 4/22/17.
 */

public class ColorsUtil {
    public static @ColorRes int getColor(String s){
        switch (s.toLowerCase()){
            case "red":return R.color.fav_red;
            case "yellow":return R.color.fav_yellow;
            case "green":return R.color.fav_green;
            case "pink":return R.color.fav_pink;
            case "white":return R.color.fav_white;
            case "blue":return R.color.fav_blue;
            case "black":return R.color.fav_black;
        }
        return R.color.fav_white;
    }

    public static @ColorRes int getTextColor(String s){
        switch (s.toLowerCase()){
            case "black":return R.color.text_white;
            default: return R.color.text_black;
        }
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


}
