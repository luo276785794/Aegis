package com.wenlong.aegis.common;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wenlong.aegis.R;
import com.wenlong.aegis.core.intercept.OnHookTouchListener;
import com.wenlong.aegis.core.interfaces.AegisIdentify;

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
    public static void hookOnTouchListener(View v) {
        if (v != null) {
            try {
                Method listenerInfoMethod = View.class.getDeclaredMethod("getListenerInfo");
                listenerInfoMethod.setAccessible(true);
                Object listenerInfo = listenerInfoMethod.invoke(v);

                Class<?> listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
                Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnTouchListener");
                onClickListenerField.setAccessible(true);
                View.OnTouchListener onTouchListener = (View.OnTouchListener) onClickListenerField.get(listenerInfo);
                if (!(onTouchListener instanceof  OnHookTouchListener)) {
                    v.setOnTouchListener(new OnHookTouchListener(onTouchListener));
                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                    ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setAccessibilityDelegate(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            View.AccessibilityDelegate accessibilityDelegate = view.getAccessibilityDelegate();
            if (!(accessibilityDelegate instanceof AegisAccessibilityDelegate)) {
                view.setAccessibilityDelegate(new AegisAccessibilityDelegate());
            }
        } else {
            view.setAccessibilityDelegate(new AegisAccessibilityDelegate());
        }
    }

    public static void setViewId(View v, ProceedingJoinPoint joinPoint) {
        if (v != null) {
            final AegisIdentify identify = ContextUtil.getAegisIdentify(joinPoint);
            if (identify != null && !TextUtils.isEmpty(identify.getIdentify())) {
                v.setTag(R.id.view_id, identify.getIdentify());
            }
        }
    }
}
