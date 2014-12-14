package com.marakana.android.yamba;

import android.os.Bundle;

public class SettingsActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this activity was created before 检查这个活动是否之前创建的
        if (savedInstanceState == null) {
            // Create a fragment 创建一个片段
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }

}
