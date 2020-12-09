package com.wenlong.aegis.core.intercept;

import com.wenlong.aegis.annotation.Aegis;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain {

    private List<Process> mChains = new ArrayList<>();
    private Aegis mAegis;

    private InterceptorChain(Aegis aegis) {
        mAegis = aegis;
    }

    public static InterceptorChain build(Aegis aegis) {
        return new InterceptorChain(aegis);
    }

    public InterceptorChain add(AegisInterceptor aegisInterceptor) {
        if (aegisInterceptor != null) {
            mChains.add(buildProcess(aegisInterceptor));
        }
        return this;
    }

    private Process buildProcess(AegisInterceptor aegisInterceptor) {
        aegisInterceptor.aegis = mAegis;
        return aegisInterceptor;
    }

    public boolean process() {
        boolean intercept = false;
        for (Process chain : mChains) {
            if (chain.intercept()) {
                intercept = true;
                break;
            }
        }
        return intercept;
    }


}
