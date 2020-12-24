package com.wenlong.aegis.core;

import android.view.View;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.intercept.AegisInterceptor;
import com.wenlong.aegis.core.interfaces.InterceptorConfig;
import com.wenlong.aegis.core.intercept.Process;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChainImpl implements InterceptorChain, InterceptorConfig {

    private final List<Process> mChains = new ArrayList<>();
    private Aegis mAegis;
    private View mView;


    @Override
    public void setView(View v) {
        mView = v;
    }

    @Override
    public void setConfig(Aegis aegis) {
        mAegis = aegis;
    }

    @Override
    public InterceptorChain add(AegisInterceptor aegisInterceptor) {
        if (aegisInterceptor != null) {
            mChains.add(aegisInterceptor);
        }
        return this;
    }

    @Override
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


    @Override
    public Aegis getAegisConfig() {
        return mAegis;
    }

    @Override
    public View getView() {
        return mView;
    }
}
