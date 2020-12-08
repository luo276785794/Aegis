package com.wenlong.aegis;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AegisAspect {

    private static final String POINTCUT_ONCLICK =
            "execution(void android.view.View.OnClickListener+.onClick(android.view.View))";
    private static final String POINTCUT_AEGIS = "execution(@com.wenlong.aegis.Aegis * *(..))";


    @Pointcut(POINTCUT_ONCLICK + "&&"+ POINTCUT_AEGIS)
    public void clickMethod() {}

    @Around("clickMethod()")
    public void aegis(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("luo", "procee");
        Aegis annotation = joinPoint.getTarget().getClass().getAnnotation(Aegis.class);
    }


}
