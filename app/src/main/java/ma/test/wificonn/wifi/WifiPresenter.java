package ma.test.wificonn.wifi;

/**
 * Created by Elmehdi Mellouk on 28/11/17.
 * XPI
 * elmehdi.mellouk@xpi.com
 */

public class WifiPresenter implements WifiContract.Presenter {
    private WifiContract.View mView;

    public WifiPresenter(WifiContract.View view) {
        mView = view;
    }

    @Override
    public void start() {

    }

}
