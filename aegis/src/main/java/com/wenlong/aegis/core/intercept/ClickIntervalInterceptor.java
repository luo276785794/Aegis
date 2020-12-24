package com.wenlong.aegis.core.intercept;


public class ClickIntervalInterceptor extends AegisInterceptor {

    public ClickIntervalInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        return false;
    }
}
