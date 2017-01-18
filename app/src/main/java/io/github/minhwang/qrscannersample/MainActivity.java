package io.github.minhwang.qrscannersample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String[] permissionsWeNeed = { Manifest.permission.CAMERA };
    private ArrayList<String> permissionsDenied = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (shouldRequestPermissions()) {
            requestPermissionsWeNeed();
        }
    }

    private boolean shouldRequestPermissions() {
        if (this.permissionsDenied == null) { this.checkPermissions(); }
        return this.permissionsDenied.size() > 0 ? true : false;
    }

    private void requestPermissionsWeNeed() {
        String[] permissions = this.permissionsDenied.toArray(new String[this.permissionsDenied.size()]);
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void checkPermissions() {
        if (this.permissionsDenied == null) { this.permissionsDenied = new ArrayList<>(); }
        for (String permission : this.permissionsWeNeed) {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                this.permissionsDenied.add(permission);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int idx = 0; idx < permissions.length; idx++) {
                if (grantResults[idx] == PackageManager.PERMISSION_GRANTED) {
                    this.permissionsDenied.remove(permissions[idx]);
                }
            }
        }
    }
}
