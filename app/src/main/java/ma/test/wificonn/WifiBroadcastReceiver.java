package ma.test.wificonn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import ma.test.wificonn.wifi.listener.OnWifiListener;

/**
 * Created by Elmehdi Mellouk on 28/11/17.
 * XPI
 * elmehdi.mellouk@xpi.com
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    OnWifiListener mOnWifiListener;

    public WifiBroadcastReceiver(OnWifiListener onWifiListener) {
        mOnWifiListener = onWifiListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {

            mOnWifiListener.resultAvailable();

        }

    }
}
