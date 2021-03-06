package com.wenlong.aegis.core.intercept;


import android.util.Log;

import com.wenlong.aegis.core.interfaces.InterceptorConfig;

import java.util.ArrayList;
import java.util.List;

public class ClickIntervalInterceptor extends AegisInterceptor {
    private static final String TAG = "IntervalInterceptor";
    private long mPreTimestamp = 0L;
    private final List<Long> mInterval = new ArrayList<>(10);
    public ClickIntervalInterceptor(InterceptorConfig config) {
        super(config);
    }

    @Override
    public boolean intercept() {
        if (config != null && config.getAegisConfig() != null) {
            boolean isIntercept = !isValid() && same();
            Log.d(TAG, "ClickIntervalInterceptor: " + isIntercept);
            return isIntercept;
        }
        return false;
    }

    private boolean same() {
        boolean same = false;
        final int size = mInterval.size();
        if (mInterval.size() >= 3) {
            final long interval1 = mInterval.get(size - 3);
            final long interval2 = mInterval.get(size - 2);
            final long interval3 = mInterval.get(size - 1);
            same = interval1 == interval2 && interval2 == interval3;
            if (same) {
                recordBarrier();
            }
            Log.d(TAG, "same interval: " + same);
            mInterval.clear();
        }
        return same;
    }

    private boolean isValid() {
        boolean isValid;
        final long currTimestamp = System.currentTimeMillis();
        final long interval = currTimestamp - mPreTimestamp;
        isValid = interval > config.getAegisConfig().interval();
        mPreTimestamp = currTimestamp;
        Log.d(TAG, "interval valid: " + isValid);

        mInterval.add(currTimestamp);
        return isValid;
    }
}
