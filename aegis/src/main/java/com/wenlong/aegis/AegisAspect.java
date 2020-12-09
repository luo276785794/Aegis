package com.wenlong.aegis;

import android.app.ActivityManager;
import android.util.Log;

import com.wenlong.aegis.annotation.Aegis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

@Aspect
public class AegisAspect {

    private static final String POINTCUT_ONCLICK =
            "execution(void android.view.View.OnClickListener+.onClick(android.view.View))";
    private static final String POINTCUT_AEGIS = "execution(@com.wenlong.aegis.annotation.Aegis * *(..))";


    @Pointcut(POINTCUT_ONCLICK + "&&"+ POINTCUT_AEGIS)
    public void clickMethod() {}

    @Around("clickMethod()")
    public void aegis(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("luo", "monkey : " + ActivityManager.isUserAMonkey());
        Object target = joinPoint.getTarget();
        if (target != null) {
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method != null && method.isAnnotationPresent(Aegis.class)) {
                    Aegis aegis = method.getAnnotation(Aegis.class);
                    Log.e("luo", String.format("interval = %d, toastMsg = %s, disableTime = %d, strategy = %s",
                            aegis.interval(), aegis.toastMsg(), aegis.disableTime(), aegis.strategy().name()));
                    break;
                }
            }
        } else {
            joinPoint.proceed();
        }

    }


}
