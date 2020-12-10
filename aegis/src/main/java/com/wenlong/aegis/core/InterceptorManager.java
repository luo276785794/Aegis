package com.wenlong.aegis.core;

public class InterceptorManager {
    private static InterceptorManager sInstance = null;

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

    private InterceptorManager() {
    }

}
