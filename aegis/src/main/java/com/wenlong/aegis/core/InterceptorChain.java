package com.wenlong.aegis.core;

import android.view.View;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.intercept.AegisInterceptor;

public interface InterceptorChain {
    void setView(View v);
    void setConfig(Aegis aegis);

    InterceptorChain add(AegisInterceptor aegisInterceptor);

    boolean process();
}
