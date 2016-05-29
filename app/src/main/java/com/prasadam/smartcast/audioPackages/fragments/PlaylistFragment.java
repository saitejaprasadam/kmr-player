package com.prasadam.smartcast.audioPackages.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prasadam.smartcast.FavoritesActivity;
import com.prasadam.smartcast.MostPlayedSongsActivity;
import com.prasadam.smartcast.R;
import com.prasadam.smartcast.RecentlyAddedActivity;
import com.prasadam.smartcast.SongPlaybackHistoryActivity;

/*
 * Created by Prasadam Saiteja on 5/18/2016.
 */

public class PlaylistFragment extends Fragment{

    private LinearLayout recentlyAddedPlaylistLinearLayout, favoritesPlaylistLinearLayout, songsPlaybackHistoryLinearLayout, mostPlayedLinearLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        recentlyAddedPlaylistLinearLayout = (LinearLayout) rootView.findViewById(R.id.recently_added_playlist);
        favoritesPlaylistLinearLayout = (LinearLayout) rootView.findViewById(R.id.favorites_playlist);
        songsPlaybackHistoryLinearLayout = (LinearLayout) rootView.findViewById(R.id.songs_playback_history);
        mostPlayedLinearLayout = (LinearLayout) rootView.findViewById(R.id.most_played_linear_layout);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recentlyAddedPlaylistLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rectenlyAddedIntent = new Intent(getContext(), RecentlyAddedActivity.class);
                startActivity(rectenlyAddedIntent);
            }
        });

        favoritesPlaylistLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritesIntent = new Intent(getContext(), FavoritesActivity.class);
                startActivity(favoritesIntent);
            }
        });

        songsPlaybackHistoryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getContext(), SongPlaybackHistoryActivity.class);
                startActivity(historyIntent);
            }
        });

        mostPlayedLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostPlayedIntent = new Intent(getContext(), MostPlayedSongsActivity.class);
                startActivity(mostPlayedIntent);
            }
        });
    }
}
