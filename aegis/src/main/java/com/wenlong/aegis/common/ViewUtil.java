package com.wenlong.aegis.common;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ViewUtil {

    public static List<View> getClickViews(ProceedingJoinPoint point) {
        List<View> views = new ArrayList<>();
        if (point != null) {
            Object[] args = point.getArgs();
            for (Object arg : args) {
                if (arg instanceof View && ((View) arg).isClickable()) {
                    views.add((View) arg);
                }
            }
        }
        return views;
    }

    public static View getClickView(ProceedingJoinPoint point) {
        List<View> clickViews = getClickViews(point);
        View v = null;
        if (!clickViews.isEmpty()) {
            v = clickViews.get(0);
        }
        return v;
    }
    @SuppressLint("DiscouragedPrivateApi")
    public static View.OnTouchListener hookOnTouchListener(ProceedingJoinPoint point) {
        View v = getClickView(point);
        if (v != null) {
            try {
                Method listenerInfoMethod = View.class.getDeclaredMethod("getListenerInfo");
                listenerInfoMethod.setAccessible(true);
                Object listenerInfo = listenerInfoMethod.invoke(v);

                Class<?> listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
                Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnTouchListener");
                onClickListenerField.setAccessible(true);
                View.OnTouchListener onTouchListener = (View.OnTouchListener) onClickListenerField.get(listenerInfo);
                if (onTouchListener != null) {

                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                    ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
