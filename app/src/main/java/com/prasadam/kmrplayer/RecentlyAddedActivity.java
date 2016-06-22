package com.prasadam.kmrplayer;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.prasadam.kmrplayer.ListenerClasses.SongsSearchListener;
import com.prasadam.kmrplayer.adapterClasses.recyclerViewAdapters.RecentlyAddedRecyclerViewAdapter;
import com.prasadam.kmrplayer.audioPackages.AudioExtensionMethods;
import com.prasadam.kmrplayer.audioPackages.modelClasses.Song;
import com.prasadam.kmrplayer.audioPackages.fragments.NoItemsFragment;
import com.prasadam.kmrplayer.sharedClasses.DividerItemDecoration;
import com.prasadam.kmrplayer.sharedClasses.ExtensionMethods;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.prasadam.kmrplayer.sharedClasses.ExtensionMethods.setStatusBarTranslucent;

/*
 * Created by Prasadam Saiteja on 5/27/2016.
 */

public class RecentlyAddedActivity extends AppCompatActivity {

    @Bind (R.id.recently_added_playlist_recycler_view) RecyclerView recentlyAddedRecyclerView;
    private ArrayList<Song> songsList;
    private RecentlyAddedRecyclerViewAdapter recyclerViewAdapter;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_recently_added_layout);
        ButterKnife.bind(this);

        new MaterialFavoriteButton.Builder(this).create();
        setStatusBarTranslucent(RecentlyAddedActivity.this);

        if(getSupportActionBar() != null )
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songsList = AudioExtensionMethods.getRecentlyAddedSongs(RecentlyAddedActivity.this);

        if(songsList.size() == 0)
        {
            NoItemsFragment newFragment = new NoItemsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(android.R.id.content, newFragment).commit();
            newFragment.setDescriptionTextView(getResources().getString(R.string.no_songs_text));
        }

        else
        {
            recyclerViewAdapter = new RecentlyAddedRecyclerViewAdapter(this, songsList);
            if (!ExtensionMethods.isTablet(this)) {
                if (!ExtensionMethods.isLandScape(this))    //Mobile Portrait
                    recentlyAddedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                if (ExtensionMethods.isLandScape(this))    //Mobile Landscape
                    recentlyAddedRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            } else {
                if (!ExtensionMethods.isLandScape(this))    //Tablet Portrait
                    recentlyAddedRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

                if (ExtensionMethods.isLandScape(this))    //Tablet Landscape
                    recentlyAddedRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
            }

            recentlyAddedRecyclerView.setAdapter(recyclerViewAdapter);
            recentlyAddedRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_recently_added_songs_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.setActionView(searchItem, searchView);
        SongsSearchListener searchListener = new SongsSearchListener(RecentlyAddedActivity.this, songsList, recentlyAddedRecyclerView, recyclerViewAdapter);
        searchView.setOnQueryTextListener(searchListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}