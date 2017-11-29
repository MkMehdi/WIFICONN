package ma.test.wificonn.wifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import ma.test.wificonn.R;
import ma.test.wificonn.WifiBroadcastReceiver;
import ma.test.wificonn.wifi.adapter.MyWifiRecyclerViewAdapter;
import ma.test.wificonn.wifi.listener.OnListFragmentInteractionListener;
import ma.test.wificonn.wifi.listener.OnWifiListener;


public class WifiFragment extends Fragment implements WifiContract.View, OnWifiListener, OnListFragmentInteractionListener {

    WifiManager mManager;
    List<ScanResult> wifiList;

    WifiBroadcastReceiver receiver;
    MyWifiRecyclerViewAdapter adapter;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;

    public static WifiFragment newInstance() {
        return new WifiFragment();
    }

    private final IntentFilter intentFilter = new IntentFilter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    }


    @Override
    public void resultAvailable() {
        Log.d("TAG", "> resultAvailable");
        startScan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_list, container, false);
        mContext = getActivity();

        mRecyclerView = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);
        Button btnScan = view.findViewById(R.id.scanBtn);
        btnScan.setOnClickListener(view1 -> startScan());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        mManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager.setWifiEnabled(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //      adapter = new MyWifiRecyclerViewAdapter(peers, mListener);
        receiver = new WifiBroadcastReceiver(this);
        mContext.registerReceiver(receiver, intentFilter);

        startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        mContext.unregisterReceiver(receiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void showProgress(boolean visibility) {
        if (progressBar != null) {
            progressBar.setVisibility((visibility) ? View.VISIBLE : View.INVISIBLE);
        }

    }

    @Override
    public void showError(int errorCode, String errorDesc) {
        showProgress(false);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }

        switch (errorCode) {
            case 0: // success
                builder.setTitle(getString(R.string.operation_successful))
                        .setMessage(errorDesc)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // continue with delete
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                break;
            case 1: // info
                builder.setTitle(getString(R.string.operation_info))
                        .setMessage(errorDesc)
                        .setPositiveButton(mContext.getString(R.string.btn_retry), (dialog, which) -> {
                            // continue with delete
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                break;
            case 2: // wifi not enable
                builder.setTitle(getString(R.string.operation_info))
                        .setMessage(errorDesc)
                        .setPositiveButton(mContext.getString(R.string.btn_setting), (dialog, which) -> {
                                    mManager.setWifiEnabled(true);
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                break;
            default: // error
                builder.setTitle(mContext.getString(R.string.operation_warning))
                        .setMessage(errorDesc)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // continue with delete
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;

        }

    }

    @Override
    public void fetchDevices() {

        mManager.startScan();
        wifiList = mManager.getScanResults();

        Log.d("TAG","size > "+wifiList.size());

    }


    @Override
    public void startScan() {
     if(mManager.isWifiEnabled()){

        showProgress(true);

        fetchDevices();

        showWifiDevices();

        showProgress(false);
     }else{
         showError(2,getString(R.string.desc_enable_wifi));
     }

    }

    @Override
    public void connect(ScanResult device,String password) {
        showProgress(true);

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", device.SSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);

        int netId = mManager.addNetwork(wifiConfig);
        mManager.disconnect();
        mManager.enableNetwork(netId, true);
        mManager.reconnect();

    }

    @Override
    public void showWifiDevices() {
        adapter = new MyWifiRecyclerViewAdapter(wifiList, this);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onListFragmentInteraction(ScanResult device) {
        Log.d("TAG ","onListFragmentInteraction>");
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }

        // Set up the password input
        final EditText input = new EditText(mContext);
        input.setHint(R.string.text_password);
        input.setWidth(70);
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        builder.setView(input);

        String wifiDeviceDetail = getString(R.string.wifi_detail,device.SSID,
                device.level,device.BSSID,device.frequency,
                device.capabilities);

        builder.setTitle(getString(R.string.operation_info))
                .setMessage(wifiDeviceDetail)
                .setPositiveButton(mContext.getString(R.string.btn_connect), (dialog, which) -> {
                    connect(device,input.getText().toString());
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    private WifiContract.Presenter mPresenter;
    @Override
    public void setPresenter(WifiContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
