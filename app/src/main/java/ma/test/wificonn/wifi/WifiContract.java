package ma.test.wificonn.wifi;

import android.net.wifi.ScanResult;

import ma.test.wificonn.BasePresenter;
import ma.test.wificonn.BaseView;

/**
 * Created by Elmehdi Mellouk on 28/11/17.
 * XPI
 * elmehdi.mellouk@xpi.com
 */

public interface WifiContract {

    interface View extends BaseView<Presenter>{
        void showProgress(boolean visibility);
        void showError(int errorCode,String errorDesc);
        void fetchDevices();
        void startScan();
        void showWifiDevices();
        void connect(ScanResult device,String password);

    }

    // No need for presenter while the app since the entire app focus about wifi manager
    interface Presenter extends BasePresenter{

    }
}
