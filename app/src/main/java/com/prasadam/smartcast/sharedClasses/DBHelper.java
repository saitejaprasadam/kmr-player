package com.prasadam.smartcast.sharedClasses;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.prasadam.smartcast.audioPackages.modelClasses.Song;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Prasadam Saiteja on 5/20/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartcast.db";
    private static final String FAVORITES_TABLE_NAME = "favorites";
    private static final String HISTORY_TABLE_NAME = "history";

    private static final String ID_COLUMN_NAME = "id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ FAVORITES_TABLE_NAME + "(id integer primary key, isFav int)");
        sqLiteDatabase.execSQL("create table "+ HISTORY_TABLE_NAME + "(id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void setFavorite(long id, boolean isFav){

        SQLiteDatabase rdb = this.getReadableDatabase();
        SQLiteDatabase wdb = this.getWritableDatabase();

        Cursor cur =  rdb.rawQuery( "select * from " + FAVORITES_TABLE_NAME +" where id = " + id + "", null );
        ContentValues contentValues = new ContentValues();
        if(isFav)
            contentValues.put("isFav", 1);
        else
            contentValues.put("isFav", 0);

        if(cur.getCount() > 0)
        {
            wdb.update(FAVORITES_TABLE_NAME, contentValues, ID_COLUMN_NAME + " = " + id, null);
            cur.close();
            rdb.close();
            wdb.close();
            return;
        }

        contentValues.put(ID_COLUMN_NAME, id);
        wdb.insert(FAVORITES_TABLE_NAME, null, contentValues);

        cur.close();
        rdb.close();
        wdb.close();
    }

    public boolean isFavorite(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery( "select * from " + FAVORITES_TABLE_NAME +" where " + ID_COLUMN_NAME + " = "+id+"", null );

        if(cur.moveToFirst()){
            if(cur.getInt(cur.getColumnIndex("isFav")) == 1)
            {
                cur.close();
                db.close();
                return true;
            }
        }

        cur.close();
        db.close();
        return false;
    }

    public ArrayList<Integer> getFavoriteSongsList(){

        ArrayList<Integer> songsID = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery( "select " + ID_COLUMN_NAME + " from " + FAVORITES_TABLE_NAME +" where isFav = 1", null);

        if(cur != null && cur.moveToFirst()){
            do {
                songsID.add(cur.getInt(cur.getColumnIndex(ID_COLUMN_NAME)));
            }while(cur.moveToNext());
        }

        if (cur != null)
            cur.close();

        db.close();
        return songsID;
    }

    public void addSongToHistory(long id){

        SQLiteDatabase wdb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_COLUMN_NAME, id);

        wdb.insert(HISTORY_TABLE_NAME, null, contentValues);
        wdb.close();
    }

    public ArrayList<Integer> getSongPlayingHistory() {

        SQLiteDatabase rdb = this.getReadableDatabase();
        Cursor cursor =  rdb.rawQuery( "select id from " + HISTORY_TABLE_NAME, null );
        ArrayList<Integer> songsID = new ArrayList<>();

        if(cursor != null && cursor.moveToLast()){
            do {
                if(!songsID.contains(cursor.getInt(cursor.getColumnIndex(ID_COLUMN_NAME))))
                    songsID.add(cursor.getInt(cursor.getColumnIndex(ID_COLUMN_NAME)));
            }while(cursor.moveToPrevious());

            return songsID;
        }

        return songsID;
    }

    public ArrayList<Song> getMostPlayedSongsList(Context context) {

        SQLiteDatabase rdb = this.getReadableDatabase();
        ArrayList<Song> mostPlayedSongs = new ArrayList<>();
        Cursor cursor =  rdb.rawQuery( "select count(*)," + ID_COLUMN_NAME +" from " + HISTORY_TABLE_NAME + " group by " + ID_COLUMN_NAME + " having count(*) > 1 order by count(*) desc", null );

        if(cursor != null && cursor.moveToFirst()){
            ContentResolver musicResolver = context.getContentResolver();
            do{

                Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor musicCursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media._ID + " = " + cursor.getInt(cursor.getColumnIndex(ID_COLUMN_NAME)) , null, null);
                if(musicCursor!=null && musicCursor.moveToFirst()){

                    long thisId = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String thisTitle = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String thisArtistID = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                    String thisArtist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String thisAlbum = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String thisAlbumID = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String thisDuration = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String thisdata = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String albumID = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String albumArtPath = null;

                    musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                    Cursor albumArtCursor = musicResolver
                            .query(musicUri, new String[]{MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{ albumID }, null);

                    if (albumArtCursor != null) {
                        if (albumArtCursor.moveToFirst())
                            albumArtPath = albumArtCursor.getString(0);
                        albumArtCursor.close();
                    }

                    Song song = new Song(thisId, thisTitle, thisArtist, thisArtistID, thisAlbum, thisAlbumID, thisDuration, thisdata, albumArtPath);
                    song.repeatCount = cursor.getInt(0);
                    mostPlayedSongs.add(song);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return mostPlayedSongs;
    }
}
