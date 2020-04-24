package com.baker.synthesis.offline.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baker.synthesis.offline.demo.permission.PermissionUtil;

public class MainActivity extends AppCompatActivity {
    private EditText edtClientId, edtClientSecret;
    private Button btnSave, btnToAudioTrackActivity, btnToMediaActivity;
    private LinearLayout lytClientInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnToAudioTrackActivity = findViewById(R.id.toAudioTrackPlayerOffline);
        btnToMediaActivity = findViewById(R.id.toMediaTrackPlayerOffline);
        lytClientInfo = findViewById(R.id.lyt_client_info);
        lytClientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        edtClientId = findViewById(R.id.edt_client_id);
        edtClientSecret = findViewById(R.id.edt_client_secret);
        btnSave = findViewById(R.id.save_client_info);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientId = edtClientId.getText().toString().trim();
                String clientSecret = edtClientSecret.getText().toString().trim();
                if (TextUtils.isEmpty(clientId)) {
                    Toast.makeText(MainActivity.this, "ClientId is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(clientSecret)) {
                    Toast.makeText(MainActivity.this, "ClientSecret is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferencesUtil.saveClientId(clientId);
                SharedPreferencesUtil.saveClientSecret(clientSecret);
                lytClientInfo.setVisibility(View.GONE);
                btnToAudioTrackActivity.setVisibility(View.VISIBLE);
                btnToMediaActivity.setVisibility(View.VISIBLE);
            }
        });

        if (TextUtils.isEmpty(SharedPreferencesUtil.getClientId())
                || TextUtils.isEmpty(SharedPreferencesUtil.getClientSecret())) {
            lytClientInfo.setVisibility(View.VISIBLE);
            btnToAudioTrackActivity.setVisibility(View.GONE);
            btnToMediaActivity.setVisibility(View.GONE);
        } else {
            lytClientInfo.setVisibility(View.GONE);
            btnToAudioTrackActivity.setVisibility(View.VISIBLE);
            btnToMediaActivity.setVisibility(View.VISIBLE);
        }
    }

    public void toAudioTrackPlayerOffline(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtil.needPermission(this, 89, Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        } else {
            startActivity(new Intent(this, OfflineAudioTrackPlayerActivity.class));
        }
    }

    public void toMediaTrackPlayerOffline(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtil.needPermission(this, 89, Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        } else {
            startActivity(new Intent(this, OfflineMediaPlayerActivity.class));
        }
    }
}
