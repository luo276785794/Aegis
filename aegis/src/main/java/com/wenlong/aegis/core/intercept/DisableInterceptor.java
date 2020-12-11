package com.wenlong.aegis.core.intercept;

public class DisableInterceptor extends AegisInterceptor {

    public DisableInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        return false;
    }
}
