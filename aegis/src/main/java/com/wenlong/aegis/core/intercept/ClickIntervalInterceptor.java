package com.wenlong.aegis.core.intercept;


import android.util.Log;

import com.wenlong.aegis.core.interfaces.InterceptorConfig;

public class ClickIntervalInterceptor extends AegisInterceptor {

    public ClickIntervalInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        Log.e("luo", "ClickIntervalInterceptor");
        return false;
    }
}
