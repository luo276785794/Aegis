package com.wenlong.aegis.common;

import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;

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
}
