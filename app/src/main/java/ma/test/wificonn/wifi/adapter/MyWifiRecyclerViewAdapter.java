package ma.test.wificonn.wifi.adapter;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ma.test.wificonn.R;
import ma.test.wificonn.wifi.listener.OnListFragmentInteractionListener;


public class MyWifiRecyclerViewAdapter extends RecyclerView.Adapter<MyWifiRecyclerViewAdapter.ViewHolder> {

    private final List<ScanResult> mValues;
    private final OnListFragmentInteractionListener mListener;
   // private Context mContext;

    public MyWifiRecyclerViewAdapter(List<ScanResult> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_wifi, parent, false);
      //  mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.wifiName.setText(holder.mItem.SSID);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView wifiName;
     //   private final TextView wifiAddress;

      private ScanResult mItem;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            wifiName = view.findViewById(R.id.wifi_name);
         //   wifiAddress = view.findViewById(R.id.wifi_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + wifiName.getText() + "'";
        }
    }
}
