package com.wenlong.aegis.core.intercept;

public class DisableInterceptor extends AegisInterceptor {
    @Override
    public boolean intercept() {
        return false;
    }
}
