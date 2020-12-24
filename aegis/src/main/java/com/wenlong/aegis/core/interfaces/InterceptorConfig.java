package com.wenlong.aegis.core.interfaces;

import android.view.View;

import com.wenlong.aegis.annotation.Aegis;

public interface InterceptorConfig {
    Aegis getAegisConfig();
    View getView();
}
