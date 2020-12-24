package com.wenlong.aegis.core.intercept;

import android.util.Log;

import com.wenlong.aegis.common.ClickEvent;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;
import com.wenlong.aegis.core.interfaces.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClickRectInterceptor extends AegisInterceptor implements TouchEvent {

    private static final String TAG = "ClickRectInterceptor";
    private List<ClickEvent> mEvents = new ArrayList<>(3);

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
            if (mEvents.size() >= 3) {
                final ClickEvent step1 = mEvents.get(0);
                final ClickEvent step2 = mEvents.get(1);
                final ClickEvent step3 = mEvents.get(2);
                boolean same = step1.equals(step2) && step2.equals(step3);
                Log.e(TAG, "ClickRectInterceptor: clear!!");
                mEvents.clear();
                return same;
            }
        } catch (Throwable ignored) {}

        return false;
    }


    @Override
    public void onEvent(ClickEvent event) {

        mEvents.add(event);
    }
}
