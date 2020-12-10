package com.wenlong.aegis;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.common.AegisAccessibilityDelegate;
import com.wenlong.aegis.common.ViewUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
public class AegisAspect {

    private static final String TAG = "Aegis";

    private static final String POINTCUT_ONCLICK =
            "execution(void android.view.View.OnClickListener+.onClick(android.view.View))";
    private static final String POINTCUT_AEGIS = "execution(@com.wenlong.aegis.annotation.Aegis * *(..))";
    private static final String POINTCUT_AEGIS2 = "call(@com.wenlong.aegis.annotation.Aegis * *(..))";


    @Pointcut(POINTCUT_ONCLICK + "&&"+ POINTCUT_AEGIS)
    public void clickMethod() {}
    @Pointcut(POINTCUT_AEGIS2)
    public void clickMethod2() {}

    @Around("clickMethod()")
    public void aegis(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e(TAG, "monkey : " + ActivityManager.isUserAMonkey());
        Object target = joinPoint.getTarget();
        if (target != null) {
            setAccessibilityDelegate(joinPoint);
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method != null && method.isAnnotationPresent(Aegis.class)) {
                    Aegis aegis = method.getAnnotation(Aegis.class);
                    Log.e(TAG, String.format("interval = %d, toastMsg = %s, disableTime = %d, strategy = %s",
                            aegis.interval(), aegis.toastMsg(), aegis.disableTime(), aegis.strategy().name()));
                    break;
                }
            }
        } else {
            joinPoint.proceed();
        }

    }

    public void setAccessibilityDelegate(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e(TAG, "accessibilityClick");
        List<View> clickViews = ViewUtil.getClickViews(joinPoint);
        for (View view : clickViews) {
            view.setAccessibilityDelegate(new AegisAccessibilityDelegate());
        }
    }




}
