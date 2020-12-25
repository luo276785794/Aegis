package com.wenlong.aegis.core.intercept;

import android.util.Log;
import android.widget.Toast;

import com.wenlong.aegis.core.ClickBarrier;
import com.wenlong.aegis.core.InterceptorManager;
import com.wenlong.aegis.core.interfaces.AegisIdentify;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;

/**
 * 禁用间隔检查
 */
public class DisableInterceptor extends AegisInterceptor {

    private static final String TAG = "DisableInterceptor";
    public DisableInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        boolean isIntercept = false;
        if (config != null) {
            final ClickBarrier clickBarrier = InterceptorManager.getInstance().getClickBarrier();
            final long barrierTime = clickBarrier.getBarrierTime(config.getView());
            final long surplus = barrierTime - System.currentTimeMillis();
            isIntercept = surplus > 0;
            Log.e(TAG, "DisableInterceptor: " + isIntercept + ", surplus: " + surplus + "ms");
            if (isIntercept && getContext() != null) {
                Toast.makeText(getContext(), config.getAegisConfig().toastMsg(), Toast.LENGTH_SHORT).show();
            }
        }
        return isIntercept;
    }
}
