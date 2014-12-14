package com.marakana.android.yamba;

import android.os.Bundle;

public class StatusActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this activity was created before 检查这个活动是否是之前创建的
        if (savedInstanceState == null) {
            // Create a fragment 创建一个片段
            StatusFragment fragment = new StatusFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }

}
