package com.wenlong.aegis.core.intercept;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wenlong.aegis.common.ClickEvent;
import com.wenlong.aegis.core.InterceptorManager;

import java.util.Locale;

/**
 * Created by Wenlong on 12/24/20.
 */
public class OnHookTouchListener implements View.OnTouchListener {

    private static final String TAG = "OnHookTouchListener";
    private final View.OnTouchListener mTargetListener;

    public OnHookTouchListener(View.OnTouchListener listener) {
        this.mTargetListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InterceptorManager.getInstance().getTouchEvent().onEvent(new ClickEvent(event.getX(), event.getY()));
            Log.d(TAG, String.format(Locale.ENGLISH, "onTouch: x = %f, y = %f", event.getX(), event.getY()));
        }
        if (mTargetListener != null) {
            return mTargetListener.onTouch(v, event);
        }
        return false;
    }
}
