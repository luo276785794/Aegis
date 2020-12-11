package com.wenlong.aegis.core.intercept;

import com.wenlong.aegis.core.InterceptorChainImpl;

public class ClickIntervalInterceptor extends AegisInterceptor {

    public ClickIntervalInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        return false;
    }
}
