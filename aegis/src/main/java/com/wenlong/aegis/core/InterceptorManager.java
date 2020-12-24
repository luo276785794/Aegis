package com.wenlong.aegis.core;

import android.view.View;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.intercept.ClickIntervalInterceptor;
import com.wenlong.aegis.core.intercept.ClickRectInterceptor;
import com.wenlong.aegis.core.intercept.DisableInterceptor;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;
import com.wenlong.aegis.core.intercept.MonkeyInterceptor;
import com.wenlong.aegis.core.interfaces.TouchEvent;

public class InterceptorManager {

    private static InterceptorManager sInstance = null;
    private final InterceptorChain mInterceptorChain;
    private final ClickRectInterceptor mClickRectInterceptor;

    private InterceptorManager() {
        mInterceptorChain = new InterceptorChainImpl();
        InterceptorConfig config = (InterceptorConfig) mInterceptorChain;
        mClickRectInterceptor = new ClickRectInterceptor(config);
        mInterceptorChain.add(new MonkeyInterceptor(config)).
                add(new DisableInterceptor(config)).
                add(new ClickIntervalInterceptor(config)).
                add(mClickRectInterceptor);

    }

    public static InterceptorManager getInstance() {
        if (sInstance == null) {
            synchronized (InterceptorManager.class) {
                if (sInstance == null) {
                    sInstance = new InterceptorManager();
                }
            }
        }
        return sInstance;
    }

    public TouchEvent getTouchEvent() {
         return mClickRectInterceptor;
    }

    public void setConfig(Aegis aegis) {
        mInterceptorChain.setConfig(aegis);
    }

    public void setView(View v) {
        mInterceptorChain.setView(v);
    }

    public boolean process() {
        return mInterceptorChain.process();
    }
}
