package com.wenlong.aegis;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.common.AegisAccessibilityDelegate;
import com.wenlong.aegis.common.ContextUtil;
import com.wenlong.aegis.common.FindHook;
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
        try {

            final Object target = joinPoint.getTarget();
            if (target != null) {
                final View v = ViewUtil.getClickView(joinPoint);
                if (v != null && v.getContext() != null && FindHook.isHook(v.getContext())) {
                    final String remind = v.getContext().getResources().getString(R.string.xposed_remind);
                    Toast.makeText(v.getContext(), remind , Toast.LENGTH_SHORT).show();
                    return;
                }
                ViewUtil.setAccessibilityDelegate(v);
                ViewUtil.hookOnTouchListener(v);
                ViewUtil.setViewId(v, joinPoint);
                if (!isIntercept(v,joinPoint)) {
                    joinPoint.proceed();
                } else if (v != null){
//                    v.setTag(R.id.barrier_time, 0);
//                    v.setTag(R.id.trigger_count, 0);
                }
            } else {
                joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            joinPoint.proceed();
        }
    }

    private synchronized boolean isIntercept(View v, ProceedingJoinPoint joinPoint) {
        boolean intercept = false;
        final Aegis aegis = getAegis(joinPoint.getTarget());
        if (aegis != null) {
            final InterceptorManager manager = InterceptorManager.getInstance();
            manager.setConfig(aegis);
            manager.setView(v);
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
                if (aegis != null) {
                    Log.d(TAG, String.format("interval = %d, toastMsg = %s, disableTime = %d, strategy = %s",
                            aegis.interval(), aegis.toastMsg(), aegis.disableTime(), aegis.strategy().name()));
                }
                break;
            }
        }
        return aegis;
    }



}
