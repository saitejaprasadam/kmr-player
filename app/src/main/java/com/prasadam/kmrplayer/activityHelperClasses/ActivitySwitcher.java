package com.prasadam.kmrplayer.activityHelperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.prasadam.kmrplayer.AlbumActivity;
import com.prasadam.kmrplayer.ArtistActivity;
import com.prasadam.kmrplayer.NearbyDevicesActivity;
import com.prasadam.kmrplayer.ExpandedAlbumartActivity;
import com.prasadam.kmrplayer.QuickShareActivity;
import com.prasadam.kmrplayer.SearchActivity;
import com.prasadam.kmrplayer.TagEditorActivity;
import com.prasadam.kmrplayer.audioPackages.modelClasses.Song;
import com.prasadam.kmrplayer.audioPackages.musicServiceClasses.MusicService;
import com.prasadam.kmrplayer.sharedClasses.KeyConstants;
import com.prasadam.kmrplayer.sharedClasses.SharedVariables;

import java.util.ArrayList;

/*
 * Created by Prasadam Saiteja on 6/24/2016.
 */

public class ActivitySwitcher {

    public static void jumpToAlbum(final Context context, final String albumTitle) {
        Intent albumActivityIntent = new Intent(context, AlbumActivity.class);
        albumActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        albumActivityIntent.putExtra("albumTitle", albumTitle);
        context.startActivity(albumActivityIntent);
    }

    public static void jumpToArtist(final Context context, final String artistTitle){
        Intent albumActivityIntent = new Intent(context, ArtistActivity.class);
        albumActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        albumActivityIntent.putExtra("artist", artistTitle);
        context.startActivity(albumActivityIntent);
    }

    public static void jumpToAlbumWithTranscition(final Activity mActivity, final ImageView imageView, final String albumTitle){

        Intent albumActivityIntent = new Intent(mActivity, AlbumActivity.class);
        albumActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, imageView, "AlbumArtImageTranscition");
        albumActivityIntent.putExtra("albumTitle", albumTitle);
        mActivity.startActivity(albumActivityIntent, options.toBundle());
    }

    public static void ExpandedAlbumArtWithTranscition(final Activity mActivity, final ImageView imageView, final String albumArtLocation){
        Intent albumActivityIntent = new Intent(mActivity, ExpandedAlbumartActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, imageView, "AlbumArtImageTranscition");
        albumActivityIntent.putExtra("albumArtPath", albumArtLocation);
        mActivity.startActivity(albumActivityIntent, options.toBundle());
    }

    public static void launchTagEditor(final Activity mActivity, final long songID, final int position){
        Intent tagEditorIntent = new Intent(mActivity, TagEditorActivity.class);
        tagEditorIntent.putExtra("songID", String.valueOf(songID));
        tagEditorIntent.putExtra("position", position);
        mActivity.startActivityForResult(tagEditorIntent, KeyConstants.TAG_EDITOR_REQUEST_CODE);
    }

    public static void initEqualizer(final Context context) {
        Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicService.player.getAudioSessionId());
        i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName());
        context.startActivity(i);
    }

    public static void jumpToAvaiableDevies(final Context context) {
        Intent avaiableDevices = new Intent(context, NearbyDevicesActivity.class);
        context.startActivity(avaiableDevices);
    }

    public static void jumpToQuickShareActivity(final Context context, final ArrayList<Song> songsList){
        Intent quickShareIntent = new Intent(context, QuickShareActivity.class);
        ArrayList<String> songsPath = new ArrayList<>();
        for (Song song : songsList){
            songsPath.add(song.getData());
        }
        quickShareIntent.putStringArrayListExtra(KeyConstants.INTENT_SONGS_PATH_LIST, songsPath);
        context.startActivity(quickShareIntent);
    }

    public static void launchSearchActivity(final Context context){
        Intent searchAcrivityIntent = new Intent(context, SearchActivity.class);
        searchAcrivityIntent.setAction(Intent.ACTION_SEARCH);
        context.startActivity(searchAcrivityIntent);
    }
}