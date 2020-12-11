package com.wenlong.aegis.core;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.intercept.AegisInterceptor;

public interface InterceptorChain {
    void setConfig(Aegis aegis);

    InterceptorChain add(AegisInterceptor aegisInterceptor);

    boolean process();
}
