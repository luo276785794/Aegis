package com.wenlong.aegis.core;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.intercept.ClickIntervalInterceptor;
import com.wenlong.aegis.core.intercept.ClickRectInterceptor;
import com.wenlong.aegis.core.intercept.DisableInterceptor;
import com.wenlong.aegis.core.intercept.InterceptorConfig;
import com.wenlong.aegis.core.intercept.MonkeyInterceptor;

public class InterceptorManager {

    private static InterceptorManager sInstance = null;
    private final InterceptorChain mInterceptorChain;

    private InterceptorManager() {
        mInterceptorChain = new InterceptorChainImpl();
        InterceptorConfig config = (InterceptorConfig) mInterceptorChain;
        mInterceptorChain.add(new MonkeyInterceptor(config)).
                add(new DisableInterceptor(config)).
                add(new ClickIntervalInterceptor(config)).
                add(new ClickRectInterceptor(config));

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


    public void setConfig(Aegis aegis) {
        mInterceptorChain.setConfig(aegis);
    }

    public boolean process() {
        return mInterceptorChain.process();
    }
}
