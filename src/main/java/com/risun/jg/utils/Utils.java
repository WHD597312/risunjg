package com.risun.jg.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by whd on 2017/12/28.
 */

public class Utils {
    public static void show(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static boolean StringIsNullOrEmpty(String s){
        return s==null || s.equals("");
    }
}
