package ma.test.wificonn.wifi;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import ma.test.wificonn.R;
import ma.test.wificonn.utils.ActivityUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WifiActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int RC_WIFI_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (hasWifiPermission()) {
            redirectWifiFragment();
        }else{
            EasyPermissions.requestPermissions(
                    this,getString(R.string.rationale_wifi),
                    RC_WIFI_PERM,LOCATION);
        }
    }

    @AfterPermissionGranted(RC_WIFI_PERM)
    private void redirectWifiFragment(){
        WifiFragment wifiFragment =
                (WifiFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (wifiFragment == null) {
            // Create the fragment
            wifiFragment = WifiFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), wifiFragment, R.id.contentFrame);
        }
    }

    private boolean hasWifiPermission() {
        return EasyPermissions.hasPermissions(this, LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasWifiPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
