package com.prasadam.kmrplayer.UI.Fragments.NetworkFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.prasadam.kmrplayer.ActivityHelperClasses.ActivityHelper;
import com.prasadam.kmrplayer.R;
import com.prasadam.kmrplayer.SocketClasses.NetworkServiceDiscovery.NSD;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by Prasadam Saiteja on 9/23/2016.
 */

public class ClientFileTransferFragment extends Fragment {

    @BindView (R.id.generic_fragment_container) FrameLayout fragmentContainer;
    @BindView (R.id.generic_recycler_view) RecyclerView recyclerView;
    private final NSD serverObject;

    public ClientFileTransferFragment(NSD serverObject){
        this.serverObject = serverObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generic_recycler_layout, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    public void onResume() {
        super.onResume();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        ActivityHelper.showEmptyFragmentChildFragment(this, "No Transfer History", fragmentContainer);
    }
}
