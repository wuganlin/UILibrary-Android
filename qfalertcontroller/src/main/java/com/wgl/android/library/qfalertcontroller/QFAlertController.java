package com.wgl.android.library.qfalertcontroller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * Created by wuganlin on 2017/3/30.
 */

public class QFAlertController extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener {
    private AlertControllerStyle mPreferredStyle;
    private FrameLayout mRootViewFl;
    private QFAlertView mAlertView;
    private QFActionSheetView mActionSheetView;

    private boolean mIsDismissed = true;
    private Animation mAlphaEnterAnimation;
    private Animation mAlphaExitAnimation;
    private Animation mAlertEnterAnimation;
    private Animation mAlertExitAnimation;
    private Animation mActionSheetEnterAnimation;
    private Animation mActionSheetExitAnimation;

    private QFAlertAction mCancelAlertAction;

    public enum AlertControllerStyle {
        ActionSheet, Alert
    }

    public QFAlertController(@NonNull Context context, int titleResId, int messageResId, AlertControllerStyle preferredStyle) {
        this(context, context.getString(titleResId), context.getString(messageResId), preferredStyle);
    }

    public QFAlertController(@NonNull Context context, CharSequence title, CharSequence message, AlertControllerStyle preferredStyle) {
        super(context, R.style.QFAlertController);
        setContentView(R.layout.ac_alert_controller);
        getWindow().setWindowAnimations(R.style.QFAlertWindow);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, getScreenHeight(context) - getStatusBarHeight(context));
        setOnShowListener(this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mPreferredStyle = preferredStyle;
        initRootView();
        initContentView(title, message);
        initAnim();
    }

    private void initRootView() {
        mRootViewFl = (FrameLayout) findViewById(R.id.fl_alert_controller_root);
        mRootViewFl.setOnClickListener(this);
        if (mPreferredStyle == AlertControllerStyle.Alert) {
            int padding = getContext().getResources().getDimensionPixelOffset(R.dimen.ac_gap) * 3;
            mRootViewFl.setPadding(padding, 0, padding, 0);
        }
    }

    private void initContentView(CharSequence title, CharSequence message) {
        if (mPreferredStyle == AlertControllerStyle.Alert) {
            mAlertView = (QFAlertView) findViewById(R.id.alertView);
            mAlertView.setAlertController(this);
            mAlertView.setTitle(title, message);
            mAlertView.setVisibility(View.VISIBLE);
        } else {
            mActionSheetView = (QFActionSheetView) findViewById(R.id.actionSheetView);
            mActionSheetView.setAlertController(this);
            mActionSheetView.setTitle(title, message);
            mActionSheetView.setVisibility(View.VISIBLE);
        }
    }

    private void initAnim() {
        mAlertEnterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_alert_enter);
        mAlertExitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_alert_exit);
        mAlertExitAnimation.setAnimationListener(new QFSimpelAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mAlertView.post(mDismissRunnable);
            }
        });

        mActionSheetEnterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_action_sheet_enter);
        mActionSheetExitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_action_sheet_exit);
        mActionSheetExitAnimation.setAnimationListener(new QFSimpelAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mActionSheetView.post(mDismissRunnable);
            }
        });

        mAlphaEnterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_alpha_enter);
        mAlphaExitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ac_alpha_exit);
        mAlphaExitAnimation.setAnimationListener(new QFSimpelAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mPreferredStyle == AlertControllerStyle.ActionSheet) {
                    mActionSheetView.startAnimation(mActionSheetExitAnimation);
                } else {
                    mAlertView.startAnimation(mAlertExitAnimation);
                }
            }
        });
    }

    // 解决 Attempting to destroy the window while drawing!
    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            QFAlertController.super.dismiss();
        }
    };

    @Override
    public void onShow(DialogInterface dialog) {
        if (mIsDismissed) {
            mIsDismissed = false;
            mRootViewFl.startAnimation(mAlphaEnterAnimation);
            if (mPreferredStyle == AlertControllerStyle.ActionSheet) {
                if (mActionSheetView.showAble()) {
                    mActionSheetView.handleBackground();
                    mActionSheetView.startAnimation(mActionSheetEnterAnimation);
                } else {
                    throw new RuntimeException("必须至少添加一个BGAActionItemView");
                }
            } else {
                if (mAlertView.showAble()) {
                    mAlertView.handleBackground();
                    mAlertView.startAnimation(mAlertEnterAnimation);
                } else {
                    throw new RuntimeException("必须至少添加一个BGAActionItemView");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fl_alert_controller_root) {
            onBackPressed();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mCancelAlertAction != null) {
            mCancelAlertAction.onClick();
        }
    }

    @Override
    public void dismiss() {
        executeExitAnim();
    }

    private void executeExitAnim() {
        if (!mIsDismissed) {
            mIsDismissed = true;
            mRootViewFl.startAnimation(mAlphaExitAnimation);
        }
    }

    /**
     * 不管添加顺序怎样，AlertActionStyle.Cancel始终是在最底部的,AlertActionStyle.Default和AlertActionStyle.Destructive按添加的先后顺序显示
     *
     * @param alertAction
     */
    public void addAction(QFAlertAction alertAction) {
        if (alertAction == null) {
            return;
        }

        if (alertAction.getTitleResId() != 0) {
            alertAction.setTitle(getContext().getString(alertAction.getTitleResId()));
        }

        if (mPreferredStyle == AlertControllerStyle.ActionSheet) {
            mActionSheetView.addAction(alertAction);
        } else {
            mAlertView.addAction(alertAction);
        }

        if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Cancel) {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            mCancelAlertAction = alertAction;
        }
    }

    private static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}