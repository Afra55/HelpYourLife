package com.example.yangshuai.alipaylife;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button openSettingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openSettingsBtn = (Button) findViewById(R.id.to_open_setting);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAccessibilitySettingsOn(this)) {
            openSettingsBtn.setText("你已经打开了权限，去支付宝使用吧");
            openSettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAlipayClient();
                }
            });
        } else {
            openSettingsBtn.setText("点我去开启权限");

            openSettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                    } else {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    }
                    startActivity(intent);
                }
            });
        }
    }

    private void openAlipayClient() {

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo("com.eg.android.AlipayGphone", 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "没有支付宝客户端", Toast.LENGTH_LONG).show();
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageInfo.packageName, className);
            intent.setComponent(cn);
            this.startActivity(intent);
        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + MyLife.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
