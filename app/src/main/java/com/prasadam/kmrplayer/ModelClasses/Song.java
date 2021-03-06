package com.prasadam.kmrplayer.ModelClasses;

import android.content.Context;

import java.io.Serializable;

import static com.prasadam.kmrplayer.AudioPackages.AudioExtensionMethods.isSongFavorite;
import static com.prasadam.kmrplayer.AudioPackages.AudioExtensionMethods.setSongFavorite;

/*
 * Created by saiteja prasadam on 2/14/2016.
 */

public class Song implements Serializable {

    private long id, duration, artistID;
    private String title, artist, album, data, albumArtLocation, hashID;
    public int repeatCount;

    public Song(long songID, String songTitle, String songArtist, long artistID, String songAlbum, long songDuration, String songData, String albumArtLocation, String hashID) {
        this.id = songID;
        this.title = songTitle;
        this.artist = songArtist;
        this.artistID = artistID;
        this.album = songAlbum;
        this.duration = songDuration;
        this.data = songData;
        this.albumArtLocation = albumArtLocation;
        this.hashID = hashID;
    }
    public Song(Song song) {
        this.id = song.getID();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.artistID = song.getArtistID();
        this.album = song.getAlbum();
        this.duration = song.getDuration();
        this.data = song.getData();
        this.albumArtLocation = song.getAlbumArtLocation();
        this.hashID = song.getHashID();
    }
    public Song(String hashID, long id) {
        this.hashID = hashID;
        this.id = id;
    }

    public long getArtistID() { return artistID; }
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum(){return album;}
    public long getDuration(){return duration;}
    public String getData(){return data;}
    public String getAlbumArtLocation(){return albumArtLocation;}
    public boolean getIsLiked(Context context) { return isSongFavorite(context, hashID); }
    public void setIsLiked(Context context, boolean value) {
        setSongFavorite(context, hashID, value);
    }
    public String getHashID(){return hashID;}
}