package com.it51buy.mojian.view;

import android.view.View;

import com.it51buy.mojian.R;
import com.it51buy.mojian.eventcenter.ECBase;
import com.it51buy.mojian.eventcenter.EventName;

public class OperationView {

    private View mOperationLayout;
    private View mTextPrompt;
    private View mSwitchLayout;
    private View mRemoveLayout;

    public OperationView(View root) {
        initView(root);
        initEvent();
    }

    private void initView(View root) {
        mOperationLayout = root.findViewById(R.id.operation_layout);
        mTextPrompt = root.findViewById(R.id.no_operation_prompt);
        mSwitchLayout = root.findViewById(R.id.switch_layout);
        mRemoveLayout = root.findViewById(R.id.remove_layout);
    }

    private void initEvent() {
        new UpdateOperationBarEvent();
    }

    /**
     * 需要传四个boolean参数，1、显示操作栏，2、显示文字提示，3、显示替换，4、显示移除
     */
    private class UpdateOperationBarEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (4 != inValues.length) {
                return;
            }

            boolean showOperationBar = (Boolean) inValues[0];
            boolean showTextView = (Boolean) inValues[1];
            boolean showSwitchView = (Boolean) inValues[2];
            boolean showRemoveView = (Boolean) inValues[3];

            mOperationLayout.setVisibility(getVisible(showOperationBar));
            mTextPrompt.setVisibility(getVisible(showTextView));
            mSwitchLayout.setVisibility(getVisible(showSwitchView));
            mRemoveLayout.setVisibility(getVisible(showRemoveView));
        }

        @Override
        public EventName pushEventName() {
            return EventName.update_operation_bar_view;
        }

    }

    private int getVisible(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

}