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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class QFActionSheetView extends LinearLayout {
    private QFAlertController mAlertController;
    private LinearLayout mContanierLl;
    private TextView mTitleTv;
    private QFActionItemView mCancelAiv;
    private boolean mIsFirstShow = true;

    public QFActionSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        mTitleTv = new TextView(context);
        mTitleTv.setGravity(Gravity.CENTER);
        mTitleTv.setBackgroundResource(android.R.color.transparent);
        int padding = getResources().getDimensionPixelOffset(R.dimen.ac_gap);
        mTitleTv.setPadding(padding * 2, padding, padding * 2, padding);
        mTitleTv.setClickable(true);

        mContanierLl = new LinearLayout(getContext());
        mContanierLl.setOrientation(VERTICAL);
        mContanierLl.setBackgroundResource(R.drawable.ac_shape_bg);
        mContanierLl.addView(mTitleTv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(mContanierLl, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setAlertController(QFAlertController alertController) {
        mAlertController = alertController;
    }

    public void setTitle(CharSequence title, CharSequence message) {
        int titleColor = getResources().getColor(R.color.ac_action_sheet_title);
        int messageColor = getResources().getColor(R.color.ac_action_sheet_message);
        int titleTextSize = getResources().getDimensionPixelOffset(R.dimen.ac_action_sheet_text_size_title);
        int messageTextSize = getResources().getDimensionPixelOffset(R.dimen.ac_action_sheet_text_size_message);
        QFAlertControllerHelper.setTitle(mTitleTv, titleColor, titleTextSize, messageColor, messageTextSize, title, message);
    }

    public void addAction(QFAlertAction alertAction) {
        QFActionItemView actionItemView = new QFActionItemView(getContext(), alertAction, mAlertController);

        if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Cancel) {
            if (mCancelAiv == null) {
                mCancelAiv = actionItemView;
                mCancelAiv.setBackgroundResource(R.drawable.ac_selector_cancel);
                MarginLayoutParams params = (MarginLayoutParams) mCancelAiv.getLayoutParams();
                params.topMargin = getResources().getDimensionPixelOffset(R.dimen.ac_gap);
                addView(actionItemView);
            } else {
                throw new RuntimeException("只能添加一个取消动作");
            }
        } else {
            mContanierLl.addView(new QFActionLineView(getContext()), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.ac_line_height)));
            mContanierLl.addView(actionItemView);
        }
    }

    public boolean showAble() {
        return mCancelAiv != null || mContanierLl.getChildCount() > 1;
    }

    public void handleBackground() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            QFAlertControllerHelper.handleActionSheetViewBackground(this);
        }
    }
}