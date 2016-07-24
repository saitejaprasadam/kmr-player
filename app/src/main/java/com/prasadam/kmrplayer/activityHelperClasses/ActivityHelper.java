package com.prasadam.kmrplayer.ActivityHelperClasses;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.prasadam.kmrplayer.AudioPackages.AudioExtensionMethods;
import com.prasadam.kmrplayer.AudioPackages.modelClasses.Song;
import com.prasadam.kmrplayer.R;
import com.prasadam.kmrplayer.Fragments.NoItemsFragment;
import com.prasadam.kmrplayer.SharedClasses.KeyConstants;

/*
 * Created by Prasadam Saiteja on 7/14/2016.
 */

public class ActivityHelper {

    public static void setDisplayHome(AppCompatActivity appCompatActivity){

        if(appCompatActivity.getSupportActionBar() != null ){
            appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static NoItemsFragment showEmptyFragment(Activity activtiy, String message){
        NoItemsFragment newFragment = new NoItemsFragment();
        FragmentTransaction ft = activtiy.getFragmentManager().beginTransaction();
        ft.add(android.R.id.content, newFragment).addToBackStack(KeyConstants.EMPTY_FRAGMENT_TAG).commit();
        newFragment.setDescriptionTextView(message);
        return newFragment;
    }

    public static NoItemsFragment showEmptyFragment(Activity activtiy, String message, FrameLayout fragmentContainer){
        NoItemsFragment newFragment = new NoItemsFragment();
        FragmentTransaction ft = activtiy.getFragmentManager().beginTransaction();
        ft.add(fragmentContainer.getId(), newFragment).addToBackStack(KeyConstants.EMPTY_FRAGMENT_TAG).commit();
        newFragment.setDescriptionTextView(message);
        return newFragment;
    }

    public static void AddToDialog(final Context context, final Song song){

        new MaterialDialog.Builder(context)
                .items(R.array.add_to_dialog_items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){

                            case 0:
                                AudioExtensionMethods.playNext(context, song);
                                break;

                            case 1:
                                AudioExtensionMethods.addToPlaylist(context, song.getHashID());
                                break;

                            case 2:
                                AudioExtensionMethods.addToNowPlayingPlaylist(context, song);
                                break;
                        }
                    }
                })
                .show();
    }
}
