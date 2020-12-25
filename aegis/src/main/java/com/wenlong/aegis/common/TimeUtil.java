package com.wenlong.aegis.common;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;

/**
 * Created by Wenlong on 12/19/20.
 */
public final class TimeUtil {

    public static long DEFAULT_INTERVAL_TIME = 2000L;

    public static int fibonacci(int i) {
        if (i == 1 || i == 2) {
            return 1;
        } else {
            return fibonacci(i - 1) + fibonacci(i - 2);
        }
    }

    public static long getIntervalTime(InterceptorConfig config, int level) {
        final long currTime = System.currentTimeMillis();
        if (config != null && config.getAegisConfig() != null) {
            final Aegis.DisableStrategy strategy = config.getAegisConfig().strategy();
            if (strategy == Aegis.DisableStrategy.Regularity) {
                return currTime + DEFAULT_INTERVAL_TIME;
            } else if (strategy == Aegis.DisableStrategy.Fibonacci) {
                return currTime + fibonacci(level) * 1000L;
            }
        }
        return currTime + DEFAULT_INTERVAL_TIME;
    }
}
