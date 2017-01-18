package com.example.yangshuai.alipaylife;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by yangshuai on 2017/1/18.
 */

public class MyLife extends AccessibilityService {
    private boolean clicking;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.i("xxxxxx", "===========");

        if (clicking) {
            return;
        }

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nextBtn = null;

        if ((nextBtn = nodeInfo.findAccessibilityNodeInfosByText("再来一次")) != null
                && (nextBtn.size() > 0)) {
            Log.i("Life", "再来一次");
            btnPerformClick(nextBtn);
        }

        if ((((nextBtn = nodeInfo.findAccessibilityNodeInfosByText("点击重试")) != null)
                && (nextBtn.size() > 0))) {
            Log.i("Life", "点击重试");
//            performGlobalAction(AccessibilityService.GESTURE_SWIPE_LEFT_AND_UP);
            btnPerformClick(nodeInfo);
        } else if ((nextBtn = nodeInfo.findAccessibilityNodeInfosByText("注意扫描的角度和距离哦")) != null && (nextBtn.size() > 0)) {
            Log.i("Life", "注意扫描的角度和距离哦");
            btnPerformClick(nodeInfo);
        } else if ((nextBtn = nodeInfo.findAccessibilityNodeInfosByText("本次扫描没有结果")) != null && (nextBtn.size() > 0)) {
            Log.i("Life", "本次扫描没有结果");
            btnPerformClick(nodeInfo);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d("Life", "onInterrupt");
    }

    private boolean btnPerformClick(AccessibilityNodeInfo nodeInfo) {
        clicking = true;
        if (nodeInfo.getChildCount() <= 0) {
            toClickAView(nodeInfo.getParent());
        } else
            toClickAView(nodeInfo);
        nodeInfo.recycle();
        clicking = false;
        return false;
    }

    private void toClickAView(final AccessibilityNodeInfo nodeInfo) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child != null && child.isClickable()) {
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
        nodeInfo.recycle();
    }

    /**
     * 模拟点击
     *
     * @param nextBtn
     * @return
     */
    private boolean btnPerformClick(List<AccessibilityNodeInfo> nextBtn) {


        AccessibilityNodeInfo nextInfo = nextBtn.get(nextBtn.size() - 1);
        if (nextInfo == null) {
            return true;
        }
        clicking = true;
        nextInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        nextInfo.recycle();
        clicking = false;
        return false;
    }
}
