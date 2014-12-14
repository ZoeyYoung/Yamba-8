package com.marakana.android.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;

public class RefreshService extends IntentService {

    private static final String TAG = RefreshService.class.getSimpleName();

    public RefreshService() {
        super(TAG);
    }

    // 当服务首次被创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    // Executes on a worker thread
    // 在一个工作线程上执行，使用IntentService的目的
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        final String username = prefs.getString("username", "");
        final String password = prefs.getString("password", "");

        // Check that username and password are not empty
        // 检查用户名和密码是否不为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Log.d(TAG, "Please update your username and password");
            // Toast.makeText(getApplicationContext(),
            //         "Please update your username and password",
            //         Toast.LENGTH_LONG).show();
            
            // create a handler to post messages to the main thread
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Please update your username and password",
                            Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        Log.d(TAG, "onStarted");

        Log.d(TAG, username);
        Log.d(TAG, password);

        ContentValues values = new ContentValues();

        YambaClient cloud = new YambaClient(username, password);
        try {
            int count = 0; // 初始化新消息的计数器为0
            List<Status> timeline = cloud.getTimeline(20);
            for (Status status : timeline) {
                values.clear();
                values.put(StatusContract.Column.ID, status.getId());
                values.put(StatusContract.Column.USER, status.getUser());
                values.put(StatusContract.Column.MESSAGE, status.getMessage());
                values.put(StatusContract.Column.CREATED_AT, status
                        .getCreatedAt().getTime());
                Uri uri = getContentResolver().insert(
                        StatusContract.CONTENT_URI, values);
                if (uri != null) {
                    count++;  // 如果有新消息，则递增计数器
                    Log.d(TAG,
                            String.format("%s: %s", status.getUser(),
                                    status.getMessage()));
                }
            }

            if (count > 0) {
                // 在至少发布了一条新消息的情况下，发送一个广播给那些关注它的人
                sendBroadcast(new Intent("com.marakana.android.yamba.action.NEW_STATUSES")
                        .putExtra("count", count));
            }

        } catch (YambaClientException e) {
            Log.e(TAG, "Failed to fetch the timeline", e);
            e.printStackTrace();
        }
        Log.d(TAG, "onHandleIntent");
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }

}
