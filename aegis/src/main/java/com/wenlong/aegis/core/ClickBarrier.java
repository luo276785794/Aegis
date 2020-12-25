package com.wenlong.aegis.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.wenlong.aegis.R;
import com.wenlong.aegis.common.TimeUtil;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;

public class ClickBarrier {
    public static final String SP_NAME = "ClickBarrier";
    private final InterceptorConfig mInterceptorConfig;
    private SharedPreferences mSharedPreferences;

    public ClickBarrier(InterceptorConfig config) {
        this.mInterceptorConfig = config;
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        if (mInterceptorConfig != null && mInterceptorConfig.getView() != null &&
                mInterceptorConfig.getView().getContext() != null) {
            final Context context = mInterceptorConfig.getView().getContext();
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public void recordBarrier(View v) {
        if (mInterceptorConfig != null && v != null) {
            final Object tag = v.getTag(R.id.view_id);
            final int level = (int) v.getTag(R.id.trigger_count);
            final long time = TimeUtil.getIntervalTime(mInterceptorConfig, level);
            if (tag != null && mSharedPreferences != null) {
                final String key = v.getId() + tag.toString();
                mSharedPreferences.edit().putLong(key, time).apply();
            } else {
                v.setTag(R.id.barrier_time, time);
            }
        }
    }

    public long getBarrierTime(View v) {
        long barrierTime = 0;
        if (v != null) {
            final Object tag = v.getTag(R.id.view_id);
            if (tag != null && mSharedPreferences != null) {
                final String key = v.getId() + tag.toString();
                barrierTime = mSharedPreferences.getLong(key, 0);
            } else if (v.getTag(R.id.barrier_time) != null){
                barrierTime = Long.parseLong(v.getTag(R.id.barrier_time).toString());
            }
        }
        return barrierTime;
    }
}
