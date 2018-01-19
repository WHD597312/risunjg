package com.risun.jg.utils;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by whd on 2017/12/22.
 */

public class AnnotationUtils {

    /**
     * 为Activity类属性赋值
     * @param className
     * @param activity
     */
    public static void findActivityById(String className, Activity activity){
        try {
            Class klass=Class.forName(className);
            Field[] fields=klass.getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                if(field.isAnnotationPresent(ViewId.class)){
                    ViewId viewId=field.getAnnotation(ViewId.class);
                    int id=viewId.id();
                    if(id>0){
                       field.set(activity,activity.findViewById(id));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
