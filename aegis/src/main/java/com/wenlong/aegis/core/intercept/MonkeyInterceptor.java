package com.wenlong.aegis.core.intercept;

import android.app.ActivityManager;
import android.util.Log;

public class MonkeyInterceptor extends AegisInterceptor {
    @Override
    public boolean intercept() {
        boolean isUserAMonkey = ActivityManager.isUserAMonkey();
        Log.d(TAG, "isUserAMonkey = " + isUserAMonkey);
        return isUserAMonkey;
    }
}
