package com.wenlong.aegis.common;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

public class AegisAccessibilityDelegate extends View.AccessibilityDelegate {
    private static final String TAG = "Aegis";
    @Override
    public boolean performAccessibilityAction(View host, int action, Bundle args) {
        if (action == AccessibilityNodeInfo.ACTION_CLICK) {
            Log.e(TAG, "AccessibilityNodeInfo.ACTION_CLICK");
            return true;
        }
        return super.performAccessibilityAction(host, action, args);
    }
}
