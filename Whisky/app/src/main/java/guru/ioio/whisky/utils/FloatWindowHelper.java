package guru.ioio.whisky.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by daniel on 7/31/17.
 */

public class FloatWindowHelper {
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    private WindowManager mWindowManager;
    private Context mContext;
    private View mContentView;

    public FloatWindowHelper(Context context, View contentView) {
        mContext = context;
        mContentView = contentView;
    }

    public boolean open() {
        //获取WindowManager
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数

        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
        wmParams.format = 1;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

        wmParams.alpha = 1.0f;

        wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
        //以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = 140;
        wmParams.height = 140;

        //显示myFloatView图像
        mWindowManager.addView(mContentView, wmParams);

        return true;
    }

    public boolean close() {
        mWindowManager.removeViewImmediate(mContentView);
        return true;
    }
}
