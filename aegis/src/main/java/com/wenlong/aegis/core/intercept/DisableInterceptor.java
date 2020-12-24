package com.wenlong.aegis.core.intercept;

import android.util.Log;

import com.wenlong.aegis.core.interfaces.InterceptorConfig;

public class DisableInterceptor extends AegisInterceptor {

    public DisableInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        Log.e("luo", "DisableInterceptor");
        return false;
    }
}
