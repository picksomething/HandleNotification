package cn.picksomething.handlenotification;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button mOpenNotify;
    private Button mCloseNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        handleNotify("open");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handleNotify("close");
    }

    private void findViews() {
        mOpenNotify = (Button) findViewById(R.id.open_notify);
        mCloseNotify = (Button) findViewById(R.id.close_notify);
    }

    private void setListeners() {
        mOpenNotify.setOnClickListener(this);
        mCloseNotify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.open_notify:
                handleNotify("open");
                break;
            case R.id.close_notify:
                handleNotify("close");
                break;
            default:
                break;
        }
    }

    private void handleNotify(String hanlde) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = null;
            if (service != null) {
                if (currentApiVersion <= 16) {
                    expand = statusbarManager.getMethod("expand");
                } else {
                    if (hanlde == "open") {
                        expand = statusbarManager
                                .getMethod("expandNotificationsPanel");
                    } else if (hanlde == "close") {
                        expand = statusbarManager
                                .getMethod("collapsePanels");
                    }
                }
                expand.setAccessible(true);
                expand.invoke(service);
            }

        } catch (Exception e) {
            Log.d("caobin", "error");
        }
    }
}
