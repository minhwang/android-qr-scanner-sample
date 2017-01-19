package io.github.minhwang.qrscannersample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

        try { openRearCamera(); }
        catch (Exception e) {}
    }

    private void openRearCamera() throws CameraAccessException, SecurityException {
        CameraManager cameraManager = (CameraManager) this.getSystemService(this.CAMERA_SERVICE);
        String[] cameras = cameraManager.getCameraIdList();
        for (String camera:cameras) {
            int face = cameraManager.getCameraCharacteristics(camera)
                                    .get(CameraCharacteristics.LENS_FACING);

            if (face == CameraCharacteristics.LENS_FACING_BACK) {
                cameraManager.openCamera(camera, new CameraStateCallback(), null);
            }
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

    private class CameraStateCallback extends CameraDevice.StateCallback {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            Log.d("min", "Opened");
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            Log.d("min", "Connected");
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            Log.d("min", "Error");
        }
    }
}
