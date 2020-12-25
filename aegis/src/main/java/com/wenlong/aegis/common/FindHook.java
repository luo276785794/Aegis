package com.wenlong.aegis.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * XPosed hook 检测
 * Created by Wenlong on 2016/11/8.
 */
public class FindHook {

    private static final boolean ON_OFF = true;
    private static final String XPOSED_PACKAGE = "de.robv.android.xposed.installer";
    private static final String XPOSED_BRIDGE = "de.robv.android.xposed.XposedBridge";

    private static boolean findHookAppName(Context context) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> applicationInfoList = packageManager
                    .getInstalledApplications(PackageManager.GET_META_DATA);
            if (applicationInfoList != null) {
                for (ApplicationInfo applicationInfo : applicationInfoList) {
                    if (XPOSED_PACKAGE.equals(applicationInfo.packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean findHookAppFile() {
        try {
            Set<String> libraries = new HashSet<>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    if (line.length() > n + 1) {
                        libraries.add(line.substring(n + 1));
                    }
                }
            }
            reader.close();
            for (String library : libraries) {
                if (library != null && library.contains("XposedBridge.jar")) {
                    return true;
                }
            }
        } catch (Exception ignored) {

        }
        return false;
    }

    /* 如果存在Xposed框架hook
     *  1）在dalvik.system.NativeStart.main方法后出现de.robv.android.xposed.XposedBridge.main调用
     *  2）如果Xposed hook了调用栈里的一个方法，
     * 还会有de.robv.android.xposed.XposedBridge.handleHookedMethod
     * 和de.robv.android.xposed.XposedBridge.invokeOriginalMethodNative调用
     */
    private static boolean findHookStack() {
        try {
            throw new Exception("findhook");
        } catch (Exception e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement == null){
                    continue;
                }
                String methodName = stackTraceElement.getMethodName();
                if (XPOSED_BRIDGE.equals(stackTraceElement.getClassName()) &&
                        ("main".equals(methodName) || "handleHookedMethod".equals(methodName))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测xposed hook
     * @param context
     * @return 如果检测到系统注入了xposed框架就停止app运行
     */
    public static boolean isHook(Context context) {
        return context != null && (findHookAppName(context) && findHookAppFile() || findHookStack());
    }


}
