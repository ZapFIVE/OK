package com.example.libnet.sample;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.libnet.R;
import com.example.libnet.sample.bean.Root;
import com.example.libnet.sample.net.NetConfig;
import com.example.libnet.sample.net.TestBaseProtocolCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Lib-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // just test 最好使用ApplicationContext
        NetConfig.INSTANCE.setContext(this);

        GitHubSearchProtocol protocol = new GitHubSearchProtocol();
        protocol.request("tetris+language:assembly", GitHubSearchProtocol.TYPE_SORT_STARS, GitHubSearchProtocol.TYPE_ORDER_DESC, new TestBaseProtocolCallback<Root>() {
            @Override
            public void onSuccess(Root data) {
                super.onSuccess(data);
                Log.d(TAG, "success data:" + data.toString() + ", isInUI:" + isThreadInUI());
            }

            @Override
            public void onFail(int errorCode) {
                super.onFail(errorCode);
                Log.d(TAG, "fail code:" + errorCode + ", isInUI:" + isThreadInUI());
            }
        });
    }

    /**
     * 是否线程运行在主线程上
     *
     * @return
     */
    private boolean isThreadInUI() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
