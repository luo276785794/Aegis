package com.wenlong.aegis;

import android.util.Log;
import android.view.View;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.common.AegisAccessibilityDelegate;
import com.wenlong.aegis.common.ViewUtil;
import com.wenlong.aegis.core.InterceptorManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
public class AegisAspect {

    private static final String TAG = "Aegis";

    private static final String POINTCUT_ONCLICK =
            "execution(void android.view.View.OnClickListener+.onClick(android.view.View))";
    private static final String POINTCUT_AEGIS = "execution(@com.wenlong.aegis.annotation.Aegis * *(..))";


    @Pointcut(POINTCUT_ONCLICK + "&&"+ POINTCUT_AEGIS)
    public void clickMethod() {}

    @Around("clickMethod()")
    public void aegis(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object target = joinPoint.getTarget();
        if (target != null) {
            setAccessibilityDelegate(joinPoint);
            final Aegis aegis = getAegis(target);
            if (!isIntercept(aegis)) {
                joinPoint.proceed();
            }
        } else {
            joinPoint.proceed();
        }

    }

    private boolean isIntercept(Aegis aegis) {
        boolean intercept = false;
        if (aegis != null) {
            final InterceptorManager manager = InterceptorManager.getInstance();
            manager.setConfig(aegis);
            intercept = manager.process();
        }
        return intercept;
    }

    private Aegis getAegis(Object target) {
        Aegis aegis = null;
        final Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method != null && method.isAnnotationPresent(Aegis.class)) {
                aegis = method.getAnnotation(Aegis.class);
                Log.d(TAG, String.format("interval = %d, toastMsg = %s, disableTime = %d, strategy = %s",
                        aegis.interval(), aegis.toastMsg(), aegis.disableTime(), aegis.strategy().name()));
                break;
            }
        }
        return aegis;
    }

    private void setAccessibilityDelegate(ProceedingJoinPoint joinPoint) {
        Log.d(TAG, "accessibilityClick");
        List<View> clickViews = ViewUtil.getClickViews(joinPoint);
        for (View view : clickViews) {
            view.setAccessibilityDelegate(new AegisAccessibilityDelegate());
        }
    }

}
