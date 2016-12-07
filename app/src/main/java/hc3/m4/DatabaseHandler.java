package hc3.m4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static String DB_PATH = "/data/data/test.test/databases/";
    private static final String DATABASE_NAME = "songsManager";

    // Songs table name
    private static final String TABLE_SONGS = "songs";

    // Songs Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ALBUM_ART = "album_art";
    private static final String KEY_GENRE = "genre";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
//            this.getReadableDatabase();

            String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                    + KEY_ARTIST + " TEXT," + KEY_ALBUM + " TEXT,"
                    + KEY_ALBUM_ART + " TEXT," + KEY_GENRE + " TEXT" + ")";
            db.execSQL(CREATE_SONGS_TABLE);
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new song
    void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle()); // Song Name
        values.put(KEY_ARTIST, song.getArtist()); // Artist
        values.put(KEY_ALBUM, song.getAlbum()); // Album
        values.put(KEY_ALBUM_ART, song.getAlbum()); // Album Art
        values.put(KEY_GENRE, song.getGenre()); // Genre

        // Inserting Row
        db.insert(TABLE_SONGS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single song
    Song getSong(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_ID,
                        KEY_TITLE, KEY_ARTIST, KEY_ALBUM, KEY_ALBUM_ART, KEY_GENRE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Song song = new Song(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        return song;
    }

    // Getting All Songs
    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setID(Integer.parseInt(cursor.getString(0)));
                song.setTitle(cursor.getString(1));
                song.setArtist(cursor.getString(2));
                song.setAlbum(cursor.getString(3));
                song.setArt(cursor.getString(4));
                song.setGenre(cursor.getString(5));
                // Adding song to list
                songList.add(song);
            } while (cursor.moveToNext());
        }

        // return song list
        return songList;
    }

    // Getting All Artists
    public List<Song> getAllArtists() {
        List<Song> artistList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT artist FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setArtist(cursor.getString(0));
                // Adding song to list
                artistList.add(song);
            } while (cursor.moveToNext());
        }

        // return artist list
        return artistList;
    }

    // Getting All Albums
    public List<Song> getAllAlbums() {
        List<Song> albumList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT album, artist FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setArtist(cursor.getString(1));
                song.setAlbum(cursor.getString(0));
                // Adding song to list
                albumList.add(song);
            } while (cursor.moveToNext());
        }

        // return albums list
        return albumList;
    }

    // Getting All Genres
    public List<Song> getAllGenres() {
        List<Song> genreList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT genre FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setGenre(cursor.getString(0));
                // Adding song to list
                genreList.add(song);
            } while (cursor.moveToNext());
        }

        // return albums list
        return genreList;
    }

    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }

    // Deleting single contact
    public void deleteSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, KEY_ID + " = ?",
                new String[] { String.valueOf(song.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getSongsCount() {
        String countQuery = "SELECT * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
