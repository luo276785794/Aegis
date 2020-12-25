package com.wenlong.aegis.core.intercept;


import android.content.Context;
import android.util.Log;
import android.view.View;

import com.wenlong.aegis.R;
import com.wenlong.aegis.core.ClickBarrier;
import com.wenlong.aegis.core.InterceptorManager;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;

import java.util.Locale;

public abstract class AegisInterceptor implements Process {
    protected static final String TAG = "AegisInterceptor";
    protected InterceptorConfig config;

    protected AegisInterceptor(InterceptorConfig config) {
        this.config = config;
    }

    protected void recordBarrier() {
        if (config != null && config.getView() != null) {
            recordTriggerCount();
            InterceptorManager.getInstance().getClickBarrier().recordBarrier(config.getView());
        }
    }

    private void recordTriggerCount() {
        final View view = config.getView();
        final Object triggerCount = view.getTag(R.id.trigger_count);
        int count = 1;
        if (triggerCount != null) {
            count = ((int) triggerCount) + 1;
        }
        Log.e(TAG, String.format(Locale.ENGLISH, "View: %s, TriggerCount:%d",
                view.toString(), count));
        view.setTag(R.id.trigger_count, count);
    }

    protected Context getContext() {
        Context context = null;
        if (config != null && config.getView() != null) {
            context = config.getView().getContext();
        }
        return context;
    }
}
