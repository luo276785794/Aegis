package com.wenlong.aegis.core.intercept;

public class ClickRectInterceptor extends AegisInterceptor {

    public ClickRectInterceptor(InterceptorConfig config) {
        super(config);

    }

    @Override
    public boolean intercept() {
        return false;
    }
}
