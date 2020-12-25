package com.wenlong.aegis.annotation;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aegis {
    /**
     * 两次点击间隔时间，默认600ms，单位：ms
     */
    long interval() default 600L;

    /**
     * 禁用时提示信息，默认不弹
     */
    String toastMsg() default "";

    /**
     * Strategy为Regularity时有效，禁用间隔时长
     */
    long disableTime() default 5000L;

    DisableStrategy strategy() default DisableStrategy.Regularity;

    /**
     * 按钮禁用时长规则
     */
    enum DisableStrategy {
        Regularity,//规律性间隔时长
        Fibonacci  //斐波那契间隔时长
    }
}
