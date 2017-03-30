/**
 * Copyright 2015 bingoogolapple
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wgl.android.library.qfalertcontroller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class QFActionItemView extends AppCompatTextView implements View.OnClickListener {
    private QFAlertAction mAlertAction;
    private QFAlertController mAlertController;

    public QFActionItemView(Context context, QFAlertAction alertAction, QFAlertController alertController) {
        super(context);
        mAlertAction = alertAction;
        mAlertController = alertController;
        int padding = getResources().getDimensionPixelOffset(R.dimen.ac_gap);
        setPadding(padding, padding, padding, padding);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (alertAction != null) {
            if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Default) {
                setTextColor(ContextCompat.getColor(context, R.color.ac_item_text_default));
            } else if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Cancel) {
                setTextColor(ContextCompat.getColor(context, R.color.ac_item_text_default));
                getPaint().setFakeBoldText(true);
            } else if (alertAction.getStyle() == QFAlertAction.AlertActionStyle.Destructive) {
                setTextColor(ContextCompat.getColor(context, R.color.ac_item_text_destructive));
            }
        }
        setGravity(Gravity.CENTER);
        setClickable(true);
        setOnClickListener(this);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelOffset(R.dimen.ac_item_text_size));
        setText(alertAction.getTitle());
    }

    @Override
    public void onClick(View v) {
        mAlertController.dismiss();
        mAlertAction.onClick();
    }
}