package hc3.m4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    private static Context context;
//    private SQLiteDatabase myDataBase;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
//    private static String DB_PATH = "/data/data/test.test/databases/";
//    private static final String DB_NAME = "songsManager.db";

    // Songs table name
    private static final String TABLE_SONGS = "songs";
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String TABLE_PLAYLISTS_SONGS = "playlists_songs";

    // Songs Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ALBUM_ART = "album_art";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_LOCAL = "local";
    private static final String KEY_PLAYLIST_NAME = "name";
    private static final String KEY_PLAYLIST_ID = "playlist_id";
    private static final String KEY_SONG_ID = "song_id";

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/test.test/databases/";

    private static String DB_NAME = "songsManager.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHandler(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() {

        boolean dbExist = checkDataBase();

        if(dbExist){
            // delete db file if already exists
//            File myFile = new File(DB_PATH+DB_NAME);
//            if(myFile.exists()) myFile.delete();
//            myFile = new File(DB_PATH+DB_NAME);
//            myDataBase = this.getReadableDatabase();
//            this.preLoadSongs();
        } else {
            myDataBase = this.getReadableDatabase();
            this.preLoadSongs();
        }

    }

    public void preLoadSongs() {

        this.addSong(new Song("Black Barbies", "Nicki Minaj", "Black Barbies", "SongPic", "Hip-Hop", 1));
        this.addSong(new Song("Light", "San Holo","Light", "SongPic", "Electronic", 1));
        this.addSong(new Song("Forever", "Adventure Club","Red//Blue", "SongPic", "Electronic", 1));
        this.addSong(new Song("Hung Up", "Tritional","Hung Up", "SongPic", "Dance", 1));
        this.addSong(new Song("Alone", "Alan Walker","Alone", "SongPic", "Electronic", 1));
        this.addSong(new Song("Redbone", "Childish Gambino","Awaken, My Love!", "SongPic", "R&B", 1));
        this.addSong(new Song("Leave", "Post Malone","Leave", "SongPic", "Rock", 1));
        this.addSong(new Song("Only One", "Sigala","Only One", "SongPic", "Dance", 1));
        this.addSong(new Song("Angel By The Wings", "Sia","Angel By The Wings", "SongPic", "Popular", 1));
        this.addSong(new Song("Find Me", "Virginia To Vegas","Utopian", "SongPic", "Pop", 1));
        this.addSong(new Song("Cat Thruster", "deadmau5","W:/2016ALBUM", "SongPic", "Popular", 1));
        this.addSong(new Song("Levitate", "Imagine Dragons","Levitate", "SongPic", "Popular", 1));
        this.addSong(new Song("U+Me", "Alx Veliz","U+Me(Noodle Remix)", "SongPic", "Electronic", 1));
        this.addSong(new Song("My Heart Is So Happy", "Cry Boy Cry","My Heart Is So Happy", "SongPic", "Dance", 1));
        this.addSong(new Song("Wicked Ways", "DVBBS","Beautiful Disaster", "SongPic", "Popular", 0));
        this.addSong(new Song("Animal Heart", "Courage My Love","Animal Heart", "SongPic", "Electronic", 1));
        this.addSong(new Song("Mutual", "David Speaker","Mutual-Single", "SongPic", "Popular", 0));
        this.addSong(new Song("Perfect Timing", "EMP","Perfect Timing", "SongPic", "Dance", 0));
        this.addSong(new Song("Galaxies", "Jenn Grant","Galaxies", "SongPic", "SongGenre", 0));
        this.addSong(new Song("Colours", "Michelle Treacy","Colours", "SongPic", "SongGenre", 0));
        this.addSong(new Song("I Know A Place", "MUNA","I Know A Place", "SongPic", "Electronic", 0));
        this.addSong(new Song("Sidewalk", "The Weekend","Starboy", "SongPic", "Popular", 1));
        this.addSong(new Song("Shed A Light(ft. Cheat Codes)", "Robin Schulz","Shed A Light", "SongPic", "Hip-Hop", 1));
        this.addSong(new Song("Hear Me Now", "Alok","Hear Me Now", "SongPic", "Popular", 1));
        this.addSong(new Song("Places", "Martin Solveig","Places", "SongPic", "Popular", 1));
        this.addSong(new Song("Let Me Love You", "DJ Snake","Let Me Love You", "SongPic", "Electronic", 0));
        this.addSong(new Song("Cigarette", "Penthox","Cigarette", "SongPic", "Dance", 1));
        this.addSong(new Song("Should've Been Me", "Naughty Boy","Shold've Been Me", "SongPic", "Popular", 0));
        this.addSong(new Song("Do with Me", "Julia Tomlinson","Do With Me", "SongPic", "Blues", 0));
        this.addSong(new Song("One Step At A Time", "Bearson","One Step At A Time", "SongPic", "Dance", 1));
        this.addSong(new Song("Can't Have", "Pitbull","Can't Have", "SongPic", "Rock", 1));
        this.addSong(new Song("You Want More", "3LAU","You Want More", "SongPic", "Hip-Hop", 0));
        this.addSong(new Song("Runaway", "ODC","Runaway", "SongPic", "Popular", 1));
        this.addSong(new Song("Shapes", "Fjord","Shapes", "SongPic", "Popular", 1));
        this.addSong(new Song("Forever Or Nothing", "NERVO","Forever Or Nothing", "SongPic", "Dance", 0));
        this.addSong(new Song("Rhythm Inside", "Calum Scott","Rhythm Inside", "SongPic", "Popular", 0));
        this.addSong(new Song("What About Me", "Isac Elliot","What About Me", "SongPic", "Popular", 0));
        this.addSong(new Song("Flirt Right Back", "Blackbear","Cashmere", "SongPic", "Electronic", 0));
        this.addSong(new Song("Touching You Again", "Hot Shade","Touching You Again", "SongPic", "Popular", 1));
        this.addSong(new Song("Rhythm Inside", "Calum Scott","Rhythm Inside", "SongPic", "Popular", 0));
        this.addSong(new Song("Black Moon", "Amaara","Black Moon", "SongPic", "Hip-Hop", 1));
        this.addSong(new Song("Who Are We?", "Amaal Nuux","Who Are We?", "SongPic", "Rock", 1));
        this.addSong(new Song("Party Monster", "The Weekend","Starboy", "SongPic", "Rock", 1));
        this.addSong(new Song("Better Off Now", "The Katherines","Better Off Now", "SongPic", "Electronic", 0));
        this.addSong(new Song("Distance Future", "Sleepy Tom","Distance Future", "SongPic", "Blues", 1));
        this.addSong(new Song("No Lies", "Sean Paul","No Lie", "SongPic", "Popular", 1));

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_ARTIST + " TEXT," + KEY_ALBUM + " TEXT,"
                + KEY_ALBUM_ART + " TEXT," + KEY_GENRE + " TEXT,"
                + KEY_LOCAL + " NUMERIC" + ")";
        db.execSQL(CREATE_SONGS_TABLE);
        String CREATE_PLAYLISTS_TABLE = "CREATE TABLE " + TABLE_PLAYLISTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYLIST_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_PLAYLISTS_TABLE);
        String CREATE_PLAYLISTS_SONGS_TABLE = "CREATE TABLE " + TABLE_PLAYLISTS_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PLAYLIST_ID + " INTEGER,"
                + KEY_SONG_ID + " INTEGER,"
                + "FOREIGN KEY (" + KEY_PLAYLIST_ID + ") REFERENCES " + TABLE_PLAYLISTS + "(" + KEY_ID + "),"
                + "FOREIGN KEY (" + KEY_SONG_ID + ") REFERENCES " + TABLE_SONGS + "(" + KEY_ID + "))";
        db.execSQL(CREATE_PLAYLISTS_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

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
        values.put(KEY_GENRE, song.getGenre()); //
        values.put(KEY_LOCAL, song.getLocal());

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
        cursor.close();
        // return contact
        return song;
    }

    // Update single song to local
    void downloadSong(String title, String artist) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_LOCAL, "1");
        String[] args = new String[]{title, artist};

        db.update(TABLE_SONGS, newValues, "title=? AND artist=?", args);

//        db.close();
    }

    // Getting All Playlists
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlistList = new ArrayList<Playlist>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS + " ORDER BY LOWER(name)";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist();
                playlist.setID(Integer.parseInt(cursor.getString(0)));
                playlist.setName(cursor.getString(1));
                // Adding song to list
                playlistList.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return song list
        return playlistList;
    }

    // Getting All Songs
    public List<Song> getAllSongs(int local) {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery;
        // Select All Query
        if (local == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_SONGS + " ORDER BY LOWER(title)";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE local = 1 ORDER BY LOWER(title)";
        }


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
                song.setLocal(Integer.parseInt(cursor.getString(6)));
                // Adding song to list
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return song list
        return songList;
    }

    // Getting All Artists
    public List<Song> getAllArtists(int local) {
        List<Song> artistList = new ArrayList<Song>();
        // Select All Query
        String selectQuery;
        if (local == 0) {
            selectQuery = "SELECT DISTINCT artist FROM " + TABLE_SONGS + " ORDER BY LOWER(artist)";
        } else {
            selectQuery = "SELECT DISTINCT artist FROM " + TABLE_SONGS + " WHERE local = 1 ORDER BY LOWER(artist)";
        }

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
        cursor.close();
        db.close();
        // return artist list
        return artistList;
    }

    // Getting All Albums
    public List<Song> getAllAlbums(int local) {
        List<Song> albumList = new ArrayList<Song>();
        // Select All Query
        String selectQuery;
        if (local == 0) {
            selectQuery = "SELECT DISTINCT album, artist FROM " + TABLE_SONGS + " ORDER BY LOWER(album)";
        } else {
            selectQuery = "SELECT DISTINCT album, artist FROM " + TABLE_SONGS + " WHERE local = 1 ORDER BY LOWER(album)";
        }

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
        cursor.close();
        db.close();
        // return albums list
        return albumList;
    }

    // Getting All Genres
    public List<Song> getAllGenres(int local) {
        List<Song> genreList = new ArrayList<Song>();
        // Select All Query
        String selectQuery;
        if (local == 0) {
            selectQuery = "SELECT DISTINCT genre FROM " + TABLE_SONGS + " ORDER BY LOWER(genre)";
        } else {
            selectQuery = "SELECT DISTINCT genre FROM " + TABLE_SONGS + " WHERE local = 1 ORDER BY LOWER(genre)";
        }

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
        cursor.close();
        db.close();
        // return albums list
        return genreList;
    }

    // Getting All Songs From Artist
    public List<Song> getAllSongsFromArtist(String artist, int local) {
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE artist=\"" + artist + "\" ORDER BY LOWER(title)";

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
        cursor.close();
        db.close();
        // return song list
        return songList;
    }

    // Getting All Songs From Album
    public List<Song> getAllSongsFromAlbum(String album, int local) {
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE album=\"" + album + "\" ORDER BY LOWER(title)";

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
        cursor.close();
        db.close();
        // return song list
        return songList;
    }

    // Getting All Songs From Genre
    public List<Song> getAllSongsFromGenre(String genre, int local) {
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE genre=\"" + genre + "\" ORDER BY LOWER(title)";

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
        cursor.close();
        db.close();
        // return song list
        return songList;
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
        db.close();
        // return count
        return cursor.getCount();
    }

    public int getPlaylistsCount() {
        String countQuery = "SELECT * FROM " + TABLE_PLAYLISTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
}
