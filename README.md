# Aegis

`Aegis`主要运用AOP技术对`onClick`插桩实现一套监控点击的工具, 名字的与美国现役先进的舰载防空和反导弹系统"宙斯盾"同名. 下面介绍下`Aegis`是如何防护"导弹"般的过度点击的.

## 刷子的手段
Android端主要有下面几种手段,
 - adb接合shell或者python脚本.
 - 自动化测试脚本.
 - Accessibility服务框架.
 - Xposed框架
 - 物理机械点击

这里面大部分功能是提供给开发与测试用的, 但功能强大, 很多黑产也运用到了这些技术. 这些技术多多少少都会有些蛛丝马迹可以追溯. 

### 原理

`Aegis`从`Xposed`, `Accessibility`, `Mokey`, 间隔时间, 坐标多个纬度检测点击状态是否是合法状态. 

在需要拦截的`View onClick`方法上增加`@Aegis`注解, 防护就会生效了.
```Java
public @interface Aegis {

    long interval() default 600L;
    String toastMsg() default "";
    long disableTime() default 5000L;
    DisableStrategy strategy() default DisableStrategy.Regularity;

    enum DisableStrategy {
        Regularity,//规律性间隔时长
        Fibonacci  //斐波那契间隔时长
    }
}
```

`Aegis`注解主要有4个自定义字段.

  - Interval
    两次点击间隔时间
 - toastMsg
    禁用时提示信息
- disableTime
    禁用间隔时长,主要与strategy字段配合使用.    
- strategy
    间隔时长规则, 包括规律性时长(每次固定时长)与按斐波那契数列增长时长
    


### 流程图
![image.png](http://pfp.ps.netease.com/kmspvt/file/5fe6fc552dcade776730957ak4XL1gcO01?sign=1hXOJuxaFUVV74i7cpBcVxvGifs=&expire=1609091906)

1. 首先需要检查`Xposed`, 通过3种方法结合来检测
    - 通过`PackageManager`遍历已安装应用是否有Xposed包名(`de.robv.android.xposed.installer`).在`Android 11`只能查询到自己应用和系统应用的信息，查不到其他应用的信息了, 必须增加`QUERY_ALL_PACKAGES`权限. 
    - 通过查找`/proc/xxxpid/maps`下的so或jar是不是包含`XposedBridge`
    - 在运行时, 主要抛出异常读异常堆栈, 如果存在Xposed框架hook,
            - 在`dalvik.system.NativeStart.main`方法后出现`de.robv.android.xposed.XposedBridge.main`调用
            - 如果Xposed hook了调用栈里的一个方法，还会有`de.robv.android.xposed.XposedBridge.handleHookedMethod`和`de.robv.android.xposed.XposedBridge.invokeOriginalMethodNative`调用

2. Accessibility检测
 一般刷子也会通过`AccessibilityService`框架写一些插件, 所以对需要监控的View增加AccessibilityDelegate监听. 主要对`AccessibilityNodeInfo.ACTION_CLICK`进行拦截
```Java
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
```
3. InterceptorProccess 运行责任链模式执行多纬度拦截器
    - MokeyInterceptor 
    Monkey是Android上的一个自动化测试工具. 通过ActivityManager的isUserAMonkey判断当前Monkey环境中
    ```Java
    /**
     * Returns "true" if the user interface is currently being messed with
     * by a monkey.
     */
    public static boolean isUserAMonkey() {
        try {
            return getService().isUserAMonkey();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
    ```
    - ClickBarrier
    ClickBarrier用于记录View禁用点击时长与次数
        - 一般只会在当前界面进行禁用时长缓存,如果想达到退出再进禁用仍然生效, 需要在View的承载页中实现`AegisIdentify`接口设置唯一id进行持久化.
            ```Java
           public interface AegisIdentify {
                 String getIdentify();
            }
           ```
        - 根据注解Aegis设置的间隔时长规则更新对应View的禁用时长.


    - DisableInterceptor
    通过ClickBarrier获取对应View是否在禁用期, 如果在此次点击失效.并弹出相应提示.
    
    - ClickIntervalInterceptor
    ClickIntervalInterceptor有2层逻辑检测,
        - 通过注解Aegis中设置的interval值,来确定两次点击是大于interval设置的间隔时间
        - 记录多次点击间隔时间, 如果连续多次点击间隔频率相同, 就会认为这是一个不正常的操作触发ClickBarrier机制.
    
    - ClickRectInterceptor
    记录多次屏幕点击坐标, 如果连续多次点击坐标相同, 就会认为这是一个不正常的操作触发ClickBarrier机制.

