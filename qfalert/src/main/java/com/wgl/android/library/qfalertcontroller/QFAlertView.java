/**
 * Copyright 2015 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wgl.android.library.qfalertcontroller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class QFAlertView extends LinearLayout {
    private QFAlertController mAlertController;
    private TextView mTitleTv;
    private QFActionItemView mCancelAiv;
    private boolean mIsFirstShow = true;
    private LinearLayout mContentLayout;
    private LinearLayout mActionLayout;

    public QFAlertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.ac_shape_bg);
        mTitleTv = new TextView(context);
        mTitleTv.setGravity(Gravity.CENTER);
        mTitleTv.setBackgroundResource(android.R.color.transparent);
        mTitleTv.setClickable(true);
        int padding = getResources().getDimensionPixelOffset(R.dimen.ac_gap);
        mTitleTv.setPadding(padding * 2, padding, padding * 2, padding);
        addView(mTitleTv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentLayout = new LinearLayout(context);
        addView(mContentLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(new QFActionLineView(getContext()), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.ac_line_height)));

        mActionLayout = new LinearLayout(context);
        mActionLayout.setOrientation(HORIZONTAL);
        addView(mActionLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

    }

    public void setAlertController(QFAlertController alertController) {
        mAlertController = alertController;
    }

    public void setTitle(CharSequence title, CharSequence message) {

        int titleColor = ContextCompat.getColor(getContext(), R.color.ac_alert_title);
        int messageColor = ContextCompat.getColor(getContext(), R.color.ac_alert_message);
        int titleTextSize = getResources().getDimensionPixelOffset(R.dimen.ac_alert_text_size_title);
        int messageTextSize = getResources().getDimensionPixelOffset(R.dimen.ac_alert_text_size_message);
        QFAlertControllerHelper.setTitle(mTitleTv, titleColor, titleTextSize, messageColor, messageTextSize, title, message);
    }

    public void addAction(QFAlertAction alertAction) {
        QFActionItemView actionItemView = new QFActionItemView(getContext(), alertAction, mAlertController);

        if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Cancel) {
            if (mCancelAiv == null) {
                mCancelAiv = actionItemView;
            } else {
                throw new RuntimeException("只能添加一个取消动作");
            }
        } else {
            addActionItemView(actionItemView);
        }
    }

    private void addActionItemView(QFActionItemView actionItemView) {
        LinearLayout.LayoutParams layoutParams0 = new LayoutParams(getResources().getDimensionPixelOffset(R.dimen.ac_line_height), getResources().getDimensionPixelOffset(R.dimen.ac_action_height));
        layoutParams0.weight = 0;
        LinearLayout.LayoutParams layoutParams1 = new LayoutParams(0, getResources().getDimensionPixelOffset(R.dimen.ac_action_height));
        layoutParams1.weight = 1;
        if (mActionLayout.getChildCount() != 0)
            mActionLayout.addView(new QFActionLineView(getContext()), layoutParams0);
        actionItemView.setBackgroundResource(R.drawable.ac_selector_center);
        mActionLayout.addView(actionItemView, layoutParams1);
    }

    public boolean showAble() {
        return mCancelAiv != null || getChildCount() > 1;
    }

    public void handleBackground() {
        if (mIsFirstShow) {
            mIsFirstShow = false;

            if (mCancelAiv != null) {
                addActionItemView(mCancelAiv);
            }

            QFAlertControllerHelper.handleAlertViewBackground(this);
        }
    }


}