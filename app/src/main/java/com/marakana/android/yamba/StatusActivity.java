package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;

public class StatusActivity extends Activity {

    private static final String TAG = StatusActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检查这个活动是否是之前创建的
        if (savedInstanceState == null) {
            // 创建一个片段
            StatusFragment fragment = new StatusFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }

}
