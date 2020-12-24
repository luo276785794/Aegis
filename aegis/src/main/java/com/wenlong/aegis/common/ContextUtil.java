package com.wenlong.aegis.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;

public final class ContextUtil {

    public static Context getContext(ProceedingJoinPoint point) {
        Context context = null;
        if (point != null) {
            Object aThis = point.getThis();
            if (aThis instanceof Activity) {
                context = (Context) aThis;
            } else if (aThis instanceof Fragment) {
                Fragment fragment = (Fragment) aThis;
                context = fragment.getContext();
            }else {
                context = getViewContext(point);
            }
        }
        return context;
    }

    private static Context getViewContext(ProceedingJoinPoint point) {
        Context context = null;
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof View) {
                context = ((View) arg).getContext();
                break;
            }
        }
        return context;
    }
}
