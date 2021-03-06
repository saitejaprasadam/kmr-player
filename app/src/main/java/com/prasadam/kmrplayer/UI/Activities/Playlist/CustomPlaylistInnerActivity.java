package com.prasadam.kmrplayer.UI.Activities.Playlist;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.prasadam.kmrplayer.ActivityHelperClasses.ActivityHelper;
import com.prasadam.kmrplayer.ActivityHelperClasses.ActivitySwitcher;
import com.prasadam.kmrplayer.ActivityHelperClasses.ShareIntentHelper;
import com.prasadam.kmrplayer.Adapters.RecyclerViewAdapters.SongsAdapter.UnifedSongAdapter;
import com.prasadam.kmrplayer.Adapters.UIAdapters.DividerItemDecoration;
import com.prasadam.kmrplayer.AudioPackages.AudioExtensionMethods;
import com.prasadam.kmrplayer.AudioPackages.BlurBuilder;
import com.prasadam.kmrplayer.AudioPackages.MusicServiceClasses.MusicPlayerExtensionMethods;
import com.prasadam.kmrplayer.R;
import com.prasadam.kmrplayer.SharedClasses.ExtensionMethods;
import com.prasadam.kmrplayer.SubClasses.CustomArrayList.SongsArrayList;
import com.prasadam.kmrplayer.UI.Activities.BaseActivity.VerticalSlidingDrawerBaseActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.prasadam.kmrplayer.AudioPackages.AudioExtensionMethods.getAlbumArtsForPlaylistCover;

/*
 * Created by Prasadam Saiteja on 5/30/2016.
 */

public class CustomPlaylistInnerActivity extends VerticalSlidingDrawerBaseActivity {

    private String playlistName;
    private UnifedSongAdapter customPlaylistSongsRecylcerViewAdapter;
    private SongsArrayList songsList;

    @BindView (R.id.fragment_container) FrameLayout FragmentContainer;
    @BindView (R.id.custom_playlist_inner_recyler_view) RecyclerView customPlaylistInnerRecyclerView;
    @BindView (R.id.background_image_view) ImageView blurredBackgroundImageView;
    @BindView (R.id.album_art_image_view1) ImageView albumartImageView1;
    @BindView (R.id.album_art_image_view2) ImageView albumartImageView2;
    @BindView (R.id.album_art_image_view3) ImageView albumartImageView3;
    @BindView (R.id.album_art_image_view4) ImageView albumartImageView4;
    @BindView (R.id.playlist_song_count_textview) TextView songCountTextView;
    @BindView (R.id.playlist_name_text_view) TextView playlistNameTextView;

    @OnClick (R.id.shuffle_fab_button)
    public void shuffleButtonClick(View view) {
        MusicPlayerExtensionMethods.shufflePlay(this, songsList);
    }

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_custom_playlist_inner_layout);
        ButterKnife.bind(this);

        setToolbar();
        playlistName = getIntent().getExtras().getString("playlistName");
        playlistNameTextView.setText(playlistName);

        int songCount = AudioExtensionMethods.getPlaylistSongCount(CustomPlaylistInnerActivity.this, playlistName);
        songCountTextView.setText(songCount + " " + getResources().getString(R.string.songs_text));

        if(songCount == 0)
            ActivityHelper.showEmptyFragment(CustomPlaylistInnerActivity.this, getResources().getString(R.string.no_songs_in_this_playlist_text, FragmentContainer), FragmentContainer);

        else{
            final ArrayList<String> albumArtPathList = getAlbumArtsForPlaylistCover(CustomPlaylistInnerActivity.this, playlistName);
            setAlbumArt(albumArtPathList);
            final MaterialDialog loading = new MaterialDialog.Builder(this)
                    .content(R.string.please_wait_while_we_populate_list_text)
                    .progress(true, 0)
                    .show();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    songsList = new SongsArrayList(AudioExtensionMethods.getSongsListFromCustomPlaylist(CustomPlaylistInnerActivity.this, playlistName)) {
                        @Override
                        public void notifyDataSetChanged() {
                            if(customPlaylistSongsRecylcerViewAdapter != null)
                                customPlaylistSongsRecylcerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void notifyItemRemoved(int index) {
                            if(customPlaylistSongsRecylcerViewAdapter != null)
                                customPlaylistSongsRecylcerViewAdapter.notifyItemRemoved(index);
                        }

                        @Override
                        public void notifyItemInserted(int index) {
                            if(customPlaylistSongsRecylcerViewAdapter != null)
                                customPlaylistSongsRecylcerViewAdapter.notifyItemInserted(index);
                        }

                        @Override
                        public void notifyItemChanged(int index) {
                            if(customPlaylistSongsRecylcerViewAdapter != null)
                                customPlaylistSongsRecylcerViewAdapter.notifyItemChanged(index);
                        }
                    };

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customPlaylistSongsRecylcerViewAdapter = new UnifedSongAdapter(CustomPlaylistInnerActivity.this, playlistName, songsList);

                            customPlaylistInnerRecyclerView.setAdapter(customPlaylistSongsRecylcerViewAdapter);
                            customPlaylistInnerRecyclerView.addItemDecoration(new DividerItemDecoration(CustomPlaylistInnerActivity.this, LinearLayoutManager.VERTICAL));

                            if (!ExtensionMethods.isTablet(CustomPlaylistInnerActivity.this)) {
                                if (!ExtensionMethods.isLandScape(CustomPlaylistInnerActivity.this))    //Mobile Portrait
                                    customPlaylistInnerRecyclerView.setLayoutManager(new LinearLayoutManager(CustomPlaylistInnerActivity.this));

                                if (ExtensionMethods.isLandScape(CustomPlaylistInnerActivity.this))    //Mobile Landscape
                                    customPlaylistInnerRecyclerView.setLayoutManager(new GridLayoutManager(CustomPlaylistInnerActivity.this, 2, GridLayoutManager.VERTICAL, false));
                            } else {
                                if (!ExtensionMethods.isLandScape(CustomPlaylistInnerActivity.this))    //Tablet Portrait
                                    customPlaylistInnerRecyclerView.setLayoutManager(new GridLayoutManager(CustomPlaylistInnerActivity.this, 2, GridLayoutManager.VERTICAL, false));

                                if (ExtensionMethods.isLandScape(CustomPlaylistInnerActivity.this))    //Tablet Landscape
                                    customPlaylistInnerRecyclerView.setLayoutManager(new GridLayoutManager(CustomPlaylistInnerActivity.this, 3, GridLayoutManager.VERTICAL, false));
                            }
                            loading.dismiss();
                        }
                    });
                }
            }).start();
        }
    }
    public void onDestroy(){
        customPlaylistInnerRecyclerView.setAdapter(null);
        customPlaylistSongsRecylcerViewAdapter = null;
        songsList = null;
        super.onDestroy();
    }

    private void setToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        toolbar.setTitleTextColor(ActivityHelper.getColor(this, R.color.white));
        toolbar.inflateMenu(R.menu.activity_custom_playlist_inner_layout);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_more_vert_white_24dp));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try{
                    int id = item.getItemId();
                    switch (id) {

                        case R.id.action_devices_button:
                            ActivitySwitcher.jumpToAvaiableDevies(CustomPlaylistInnerActivity.this);
                            break;

                        case R.id.action_equilzer:
                            ActivitySwitcher.initEqualizer(CustomPlaylistInnerActivity.this);
                            break;

                        case R.id.action_quick_share:
                            ActivitySwitcher.jumpToQuickShareActivity(CustomPlaylistInnerActivity.this, songsList);
                            break;

                        case R.id.action_share_playlist:
                            ShareIntentHelper.sharePlaylist(CustomPlaylistInnerActivity.this, songsList, playlistName);
                            break;

                        case R.id.action_search:
                            ActivitySwitcher.launchSearchActivity(CustomPlaylistInnerActivity.this);
                            break;

                        case R.id.action_add_to_now_playing_queue:
                            MusicPlayerExtensionMethods.addToNowPlayingPlaylist(CustomPlaylistInnerActivity.this, songsList, "Playlist songs added to now playing playlist");
                            break;

                        case R.id.action_play_next:
                            MusicPlayerExtensionMethods.playNext(CustomPlaylistInnerActivity.this, songsList, "Playlist will be played next");
                            break;
                    }
                }
                catch (Exception ignored){}
                return true;
            }
        });

        toolbar.setPadding(0, ActivityHelper.getStatusBarHeight(this), 0, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }
    private void setAlbumArt(final ArrayList<String> albumArtPathList) {
        File imgFile;

        if(albumArtPathList.size() > 0){
            String albumArtPath = albumArtPathList.get(0);
            if(albumArtPath != null)
            {
                imgFile = new File(albumArtPath);
                if(imgFile.exists()){
                    albumartImageView1.setImageURI(Uri.parse("file://" + imgFile.getAbsolutePath()));
                    blurredBackgroundImageView.setImageBitmap(BlurBuilder.blur(CustomPlaylistInnerActivity.this, ((BitmapDrawable) albumartImageView1.getDrawable()).getBitmap()));
                }
            }
        }

        if(albumArtPathList.size() > 1){
            String albumArtPath = albumArtPathList.get(1);
            if(albumArtPath != null)
            {
                imgFile = new File(albumArtPath);
                if(imgFile.exists())
                    albumartImageView2.setImageURI(Uri.parse("file://" + imgFile.getAbsolutePath()));
            }
        }

        if(albumArtPathList.size() > 2){
            String albumArtPath = albumArtPathList.get(2);
            if(albumArtPath != null)
            {
                imgFile = new File(albumArtPath);
                if(imgFile.exists())
                    albumartImageView3.setImageURI(Uri.parse("file://" + imgFile.getAbsolutePath()));
            }
        }

        if(albumArtPathList.size() > 3){
            String albumArtPath = albumArtPathList.get(3);
            if(albumArtPath != null)
            {
                imgFile = new File(albumArtPath);
                if(imgFile.exists())
                    albumartImageView4.setImageURI(Uri.parse("file://" + imgFile.getAbsolutePath()));
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityHelper.onActivityResultMethod(this, requestCode, resultCode, data, songsList);
    }
}
