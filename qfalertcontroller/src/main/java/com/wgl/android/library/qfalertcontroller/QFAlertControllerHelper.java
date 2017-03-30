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

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class QFAlertControllerHelper {

    private QFAlertControllerHelper() {
    }

    public static void setTitle(TextView titleTv, int titleColor, int titleTextSize, int messageColor, int messageTextSize, CharSequence title, CharSequence message) {
        titleTv.setMinHeight(titleTextSize * 3);

        if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(message)) {
            titleTv.setTextColor(titleColor);
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            titleTv.getPaint().setFakeBoldText(true);
            titleTv.setText(title);
        } else if (TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            titleTv.setTextColor(messageColor);
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, messageTextSize);
            titleTv.setText(message);
        } else if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            SpannableString titleSs = new SpannableString(title + "\n" + message);
            titleSs.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleSs.setSpan(new AbsoluteSizeSpan(titleTextSize), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleSs.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            titleSs.setSpan(new ForegroundColorSpan(messageColor), title.length(), titleSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleSs.setSpan(new AbsoluteSizeSpan(messageTextSize), title.length(), titleSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleTv.setText(titleSs);
            titleTv.setLineSpacing(0.0f, 1.2f);
        } else {
            titleTv.setVisibility(View.GONE);
        }
    }

    public static void handleAlertViewBackground(@NonNull QFAlertView alertView) {
        if (alertView.getChildCount() < 4) return;
        if (alertView.getChildAt(3) instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) alertView.getChildAt(3);
            if (container.getChildCount() == 0) return;
            if (container.getChildCount() == 1) {
                if (container.getChildAt(0) instanceof QFActionItemView) {
                    QFActionItemView itemView = (QFActionItemView) container.getChildAt(0);
                    itemView.setBackgroundResource(R.drawable.ac_selector_bottom);
                }
                return;
            }
            for (int i = 0; i < container.getChildCount(); i++) {
                if (container.getChildAt(i) instanceof QFActionItemView) {
                    QFActionItemView itemView = (QFActionItemView) container.getChildAt(i);
                    if (i == 0) {
                        itemView.setBackgroundResource(R.drawable.ac_selector_bottom_left);
                    } else if (i == container.getChildCount() - 1) {
                        itemView.setBackgroundResource(R.drawable.ac_selector_bottom_right);
                    } else {
                        itemView.setBackgroundResource(R.drawable.ac_selector_center);
                    }
                }
            }
        }
    }


    public static void handleActionSheetViewBackground(@NonNull QFActionSheetView actionSheetView) {
        if (actionSheetView.getChildCount() == 0) return;
        if (!(actionSheetView.getChildAt(0) instanceof ViewGroup)) return;
        ViewGroup container = (ViewGroup) actionSheetView.getChildAt(0);
        if (container.getChildAt(0).getVisibility() == View.VISIBLE) {
            // 有标题的情况

            if (container.getChildCount() >= 3) {
                for (int i = 2; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (child instanceof QFActionItemView) {
                        if (i == container.getChildCount() - 1) {
                            child.setBackgroundResource(R.drawable.ac_selector_bottom);
                        } else {
                            child.setBackgroundResource(R.drawable.ac_selector_center);
                        }
                    }
                }
            }
        } else {
            // 没有标题的情况

            if (container.getChildCount() == 3) {
                // 只有一个BGAActionItemView的情况

                // 移除第一条分割线
                container.removeViewAt(1);

                container.getChildAt(1).setBackgroundResource(R.drawable.ac_selector_cancel);
            } else if (container.getChildCount() > 3) {
                // 大于一个BGAActionItemView的情况

                // 移除第一条分割线
                container.removeViewAt(1);

                for (int i = 1; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (child instanceof QFActionItemView) {
                        if (i == 1) {
                            child.setBackgroundResource(R.drawable.ac_selector_top);
                        } else if (i == container.getChildCount() - 1) {
                            child.setBackgroundResource(R.drawable.ac_selector_bottom);
                        } else {
                            child.setBackgroundResource(R.drawable.ac_selector_center);
                        }
                    }
                }
            }
        }
    }
}