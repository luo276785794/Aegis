package com.wenlong.aegis.core.intercept;

import android.util.Log;

import com.wenlong.aegis.core.InterceptorManager;
import com.wenlong.aegis.core.interfaces.AegisIdentify;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;

public class DisableInterceptor extends AegisInterceptor {

    public DisableInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        AegisIdentify identify = InterceptorManager.getInstance().getAegisIdentify();
        if (identify != null) {

        }
        Log.e("luo", "DisableInterceptor");
        return false;
    }
}
