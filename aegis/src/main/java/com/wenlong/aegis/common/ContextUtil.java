package com.wenlong.aegis.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.wenlong.aegis.core.interfaces.AegisIdentify;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ContextUtil {

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class));

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

    public static Object getThisContext(ProceedingJoinPoint point) {
        Object target = point.getTarget();
        try {
            Field field = target.getClass().getDeclaredField("this$0");
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AegisIdentify getAegisIdentify(ProceedingJoinPoint point) {
        Object obj = getThisContext(point);
        if (obj instanceof AegisIdentify) {
            AegisIdentify aegisIdentify = (AegisIdentify) obj;
            Object identify = aegisIdentify.getIdentify();
            if (identify != null && !isWrapperType(identify.getClass())) {
                throw new IllegalArgumentException("Aegis identify only primitive type");
            }
            return (AegisIdentify) obj;
        }
        return null;
    }


    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
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
