package com.wenlong.aegis.core.intercept;

import android.util.Log;

import com.wenlong.aegis.common.ClickEvent;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;
import com.wenlong.aegis.core.interfaces.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class ClickRectInterceptor extends AegisInterceptor implements TouchEvent {

    private static final String TAG = "ClickRectInterceptor";
    private final List<ClickEvent> mEvents = new ArrayList<>(3);
    private final Object lock = new Object();

    public ClickRectInterceptor(InterceptorConfig config) {
        super(config);

    }

    @Override
    public boolean intercept() {
        final boolean checkClick = checkClick();
        Log.e(TAG, "ClickRectInterceptor: " + checkClick);
        return checkClick;
    }

    private boolean checkClick() {
        try {
            synchronized (lock) {
                final int size = mEvents.size();
                if (size >= 3) {
                    final ClickEvent step1 = mEvents.get(size - 3);
                    final ClickEvent step2 = mEvents.get(size - 2);
                    final ClickEvent step3 = mEvents.get(size - 1);
                    boolean same = step1.equals(step2) && step2.equals(step3);
                    if (same) {
                        recordBarrier();
                    }
                    Log.e(TAG, "ClickRectInterceptor: clear!!" + "size:" + size);
                    mEvents.clear();
                    return same;
                }
            }
        } catch (Throwable ignored) {}

        return false;
    }


    @Override
    public void onEvent(ClickEvent event) {
        synchronized (lock) {
            mEvents.add(event);
            Log.e(TAG, "ClickRectInterceptor: add size = " + mEvents.size());
        }
    }
}
