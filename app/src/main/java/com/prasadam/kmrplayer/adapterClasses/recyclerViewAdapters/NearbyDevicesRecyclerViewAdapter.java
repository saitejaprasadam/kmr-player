package com.prasadam.kmrplayer.adapterClasses.recyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prasadam.kmrplayer.R;
import com.prasadam.kmrplayer.sharedClasses.KeyConstants;
import com.prasadam.kmrplayer.socketClasses.NSDClient;
import com.prasadam.kmrplayer.socketClasses.QuickShareClient;
import com.prasadam.kmrplayer.socketClasses.ServerThread;
import com.prasadam.kmrplayer.socketClasses.SocketExtensionMethods;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * Created by Prasadam Saiteja on 7/5/2016.
 */

public class NearbyDevicesRecyclerViewAdapter extends RecyclerView.Adapter<NearbyDevicesRecyclerViewAdapter.ViewAdapter>{

    private ArrayList<String> QuickSharePathList;
    private LayoutInflater inflater;
    private Activity mActivity;
    private int count = 0;

    public NearbyDevicesRecyclerViewAdapter(Activity mActivity, Context context){
        this.mActivity = mActivity;
        inflater = LayoutInflater.from(context);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public NearbyDevicesRecyclerViewAdapter.ViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_near_by_devices, parent, false);
        return new ViewAdapter(view);
    }
    public void onBindViewHolder(NearbyDevicesRecyclerViewAdapter.ViewAdapter holder, int position) {

        final NsdServiceInfo serverObject = NSDClient.devicesList.get(position);
        holder.nearbyDeviceNameTextView.setText(serverObject.getServiceName());
        if(mActivity.getClass().getSimpleName().equals(KeyConstants.ACTIVITY_QUICK_SHARE))
            setHolderQuickShareActivity(holder, serverObject);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            byte[] temp = serverObject.getAttributes().get(KeyConstants.DEVICE_TYPE);
            if (temp != null && temp.toString().equals(KeyConstants.MOBILE))
                holder.nearbyDevicesImageView.setImageResource(R.mipmap.android_device);
            else if (temp != null && temp.toString().equals(KeyConstants.TABLET))
                holder.nearbyDevicesImageView.setImageResource(R.mipmap.macbook_pro);
            else
                holder.nearbyDevicesImageView.setImageResource(getDeviceImage());
        }

        else*/
        holder.nearbyDevicesImageView.setImageResource(getDeviceImage());
    }
    public int getItemCount() {
        return NSDClient.devicesList.size();
    }

    public int getDeviceImage(){
        switch (count){

            case 0:
                count++;
                return R.mipmap.android_device;

            case 1:
                count++;
                return R.mipmap.apple_mac;

            case 2:
                count++;
                return R.mipmap.iphone_6s;

            case 3:
                count++;
                return R.mipmap.macbook_pro;

            default:
                count = 0;
                return R.mipmap.apple_mac;
        }
    }
    public void setQuickShareSongPathList(ArrayList<String> songPathList){
        this.QuickSharePathList = songPathList;
    }
    private void setHolderQuickShareActivity(ViewAdapter holder, final NsdServiceInfo serverObject) {
        holder.nearbyDevicesContextMenu.setImageResource(R.mipmap.ic_chevron_right_black_24dp);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickShareClient quickShareClient= new QuickShareClient(serverObject.getHost(), QuickSharePathList);
                quickShareClient.execute();
                Toast.makeText(mActivity, "Initiating transfer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewAdapter extends RecyclerView.ViewHolder{

        @Bind(R.id.rootLayout_recycler_view) RelativeLayout rootLayout;
        @Bind(R.id.nearby_devices_album_art) ImageView nearbyDevicesImageView;
        @Bind(R.id.nearby_devices_context_menu) ImageView nearbyDevicesContextMenu;
        @Bind(R.id.device_name_textview) TextView nearbyDeviceNameTextView;

        public ViewAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
