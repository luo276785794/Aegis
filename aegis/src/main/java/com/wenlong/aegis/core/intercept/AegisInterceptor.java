package com.wenlong.aegis.core.intercept;


import com.wenlong.aegis.core.interfaces.InterceptorConfig;

public abstract class AegisInterceptor implements Process {
    protected static final String TAG = "AegisInterceptor";
    public InterceptorConfig config;

    public AegisInterceptor(InterceptorConfig config) {
        this.config = config;
    }
    
}
