package com.prasadam.kmrplayer;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.prasadam.kmrplayer.AdapterClasses.RecyclerViewAdapters.NowPlayingPlaylistAdapter;
import com.prasadam.kmrplayer.AdapterClasses.UIAdapters.NowPlayingPlaylistInterfaces;
import com.prasadam.kmrplayer.AdapterClasses.UIAdapters.SimpleItemTouchHelperCallback;
import com.prasadam.kmrplayer.SharedClasses.SharedVariables;

/*
 * Created by Prasadam Saiteja on 6/30/2016.
 */

public class NowPlayingPlaylistActivity extends AppCompatActivity implements NowPlayingPlaylistInterfaces.OnStartDragListener {

    public static RecyclerView nowPlayingPlaylistRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_now_playing_playlist);
        nowPlayingPlaylistRecyclerView = (RecyclerView) findViewById(R.id.now_playing_playlist_recycler_view);
        setRecyclerViewAdapter();
    }
    public void onResume() {
        super.onResume();
        SharedVariables.globalActivityContext = this;
    }

    private void setRecyclerViewAdapter() {
        final NowPlayingPlaylistAdapter recyclerViewAdapter = new NowPlayingPlaylistAdapter(this, this);
        nowPlayingPlaylistRecyclerView.setAdapter(recyclerViewAdapter);
        nowPlayingPlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(NowPlayingPlaylistActivity.this));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(nowPlayingPlaylistRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
