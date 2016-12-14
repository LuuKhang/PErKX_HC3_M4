package hc3.m4;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.support.v4.app.ListFragment;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LocalLibrary extends AppCompatActivity implements MediaPlayerControl {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static int currentPage = 0;


    // Music service, to play music in the background ---------------------------
    private MusicService musicService;
    private MediaController musicController;
    private ArrayList<Song> songsToPlay;
    private int currentTrack;
    private boolean musicBound = false;
    private Intent playIntent;
    private boolean paused = false, playbackPaused = false;

    private TextView curSongTitle;
    private TextView curArtist;
    private ToggleButton playPauseButton;
    // -------------------------------------------------------------------------



    public static String categoryTitle;

    // To keep track of the data in each list so that search can modify it
    static SongAdapter songAdapterSongs;
    static SongAdapter songAdapterArtists;
    static SongAdapter songAdapterAlbums;
    static SongAdapter songAdapterGenres;
    static SongAdapter songAdapter; // General list for detailed inner lists (ex. selecting an artist)

    static PlaylistAdapter playlistAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_local_library);

        // Initialize toolbar (top most portion of screen)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PErKX - Local Library"); // Set toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                SongList fragment = (SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                if (fragment != null) {
                    fragment.update();
                }
                currentPage = position;
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // Common navigation buttons along buttom
        Button btn_online = (Button) findViewById(R.id.btn_online);
        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocalLibrary.this, OnlineSection.class)); // Opens Online Section
            }
        });

        // Database handler
        DatabaseHandler db = new DatabaseHandler(this);
        db.createDataBase();



        // Music Controller set up --------------------------------------------
        curSongTitle = (TextView) findViewById(R.id.songTitle);
        curArtist = (TextView) findViewById(R.id.artistName);
        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);

        currentTrack = getIntent().getIntExtra("currentTrackNumber", 0);
        songsToPlay = new ArrayList<Song>();
        ArrayList<Song> songsList = (ArrayList<Song>) getIntent().getSerializableExtra("playlistSongs");
        if ( songsList != null )
            songsToPlay.addAll( songsList );

        setController();
        // ---------------------------------------------------------------
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backButton(View view) {
        SongList fragment = (SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.update();
        }
    }



    // Music Controller classes, to play/pause/control ------------------------
    public void songSelected(View view){
        // Sets all the shown songs in the music player
        songsToPlay = new ArrayList<Song>();
        currentTrack = Integer.parseInt(view.getTag().toString());
        switch (currentPage) {
            case 0:
                if (playlistAdapter != null) {
                    songsToPlay.addAll(playlistAdapter.getAllSongs());
                }
                break;
            case 1:
                if (songAdapterSongs != null) {
                    songsToPlay.addAll(songAdapterSongs.getAll());
                }
                break;
            case 2:
            case 3:
            case 4:
                if (songAdapter != null) {
                    songsToPlay.addAll(songAdapter.getAll());
                }
                break;
        }

        if (currentTrack == -1) { // If shuffle all was selected, start at a random position
            currentTrack = new Random().nextInt(songsToPlay.size());
        }
        musicService.setSong(currentTrack);
        musicService.setList(songsToPlay);

        musicService.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }

        updateCurTrack(true);

        //musicController.show(0);
    }
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsToPlay);
            musicBound = true;

            updateCurTrack(false);

            //musicController.show(0);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    private void setController() {
        musicController = new MediaController(this);
        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.localLibraryRelativeLayout));
        musicController.setEnabled(true);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
    }
    @Override
    public void start() {
        musicService.go();
    }
    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getDur();
        else return 0;
    }
    @Override
    public int getCurrentPosition() {
        return musicService.getPosn();
    }
    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }
    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPng();
        return false;
    }
    @Override
    public int getBufferPercentage() {
        return 0;
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() {
        return true;
    }
    @Override
    public boolean canSeekForward() {
        return true;
    }
    @Override
    public int getAudioSessionId() {
        return 0;
    }
    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
        System.exit(0);
    }
    //play next
    public void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        //musicController.show(0);
    }
    public void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        //musicController.show(0);
    }


    // Function used to show play page
    // Current set to open new activity, but I think that's horrible practice. Might switch to play page = fragment
    public void openPlayPage() {
        Intent playPage = new Intent(LocalLibrary.this, PlayPage.class);
        playPage.putExtra("playlistSize", songsToPlay.size());
        playPage.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
        playPage.putExtra("playlistSongs", songsToPlay);
        startActivity(playPage); // Opens Play Page
    }
    public void updateCurTrack(boolean forSureIsPlaying) {
        curArtist.setText("By: " + musicService.getArtist());
        curSongTitle.setText(musicService.getSongTitle());

        if (forSureIsPlaying)
            playPauseButton.setChecked(true);
        else
            playPauseButton.setChecked(musicService.isPng());
    }
    // ----------------------------------------------------------------------------------------



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return SongList.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5; // Number of tabs (pages)
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PLAYLIST";
                case 1:
                    return "SONG";
                case 2:
                    return "ARTIST";
                case 3:
                    return "ALBUM";
                case 4:
                    return "GENRE";
            }
            return null;
        }
    }

    // Our ListFragment class, shows the items on each tab of Local Library
    public static class SongList extends ListFragment {

        // Key defined, for id of current tab
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int level = 0;
        private static LayoutInflater inflater;

        // Returns a new instance of this fragment for the given section number.
        public static SongList newInstance(int sectionNumber) {
            SongList fragment = new SongList();

            // Arguments/parameters that each tab/section/fragment will have, example: ID
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.inflater = inflater;
            update();

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void update() {
            setHasOptionsMenu(true);

            DatabaseHandler db = new DatabaseHandler(inflater.getContext());

//            List<Song> songs = db.getAllSongs();
            Log.d("level: ", String.valueOf(level));
            // ID number of the current section (label and id mapping may change)
            //  Playlist = 1
            //  Song = 2
            //  Artist = 3
            //  Album = 4
            //  Genre = 5
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                case 1:
                    List<Playlist> playlists = db.getAllPlaylists();
                    playlistAdapter = new PlaylistAdapter(inflater.getContext(), 0, playlists);
                    if (playlistAdapter != null) setListAdapter(playlistAdapter);
                    level = 0;
                    Log.d("page: ", "playlist");
                    break;
                case 2:
                    List<Song> songs = db.getAllSongs(1);
                    songAdapterSongs = new SongAdapter(inflater.getContext(), sectionNumber, songs);
                    // Setting the list adapter for the ListFragment
                    if (songAdapterSongs != null) setListAdapter(songAdapterSongs);
                    level = 0;
                    Log.d("page: ", "song");
                    break;
                case 3:
                    List<Song> artists = db.getAllArtists(1);
                    songAdapterArtists = new SongAdapter(inflater.getContext(), sectionNumber, artists);
                    // Setting the list adapter for the ListFragment
                    if (songAdapterArtists != null) setListAdapter(songAdapterArtists);
                    level = 0;
                    Log.d("page: ", "artist");
                    break;
                case 4:
                    List<Song> albums = db.getAllAlbums(1);
                    songAdapterAlbums = new SongAdapter(inflater.getContext(), sectionNumber, albums);
                    // Setting the list adapter for the ListFragment
                    if (songAdapterAlbums != null) setListAdapter(songAdapterAlbums);
                    level = 0;
                    Log.d("page: ", "album");
                    break;
                case 5:
                    List<Song> genres = db.getAllGenres(1);
                    songAdapterGenres = new SongAdapter(inflater.getContext(), sectionNumber, genres);
                    // Setting the list adapter for the ListFragment
                    if (songAdapterGenres != null) setListAdapter(songAdapterGenres);
                    level = 0;
                    Log.d("page: ", "genre");
                    break;
            }

        }

        // Function called when a tab's list view item is clicked
        @Override
        public void onListItemClick(ListView listview, View view, int pos, long id) {
            DatabaseHandler db = new DatabaseHandler(view.getContext());
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            TextView title = (TextView) view.findViewById(R.id.title);
            switch (sectionNumber) { // Depending current tab, different action
                // Selecting a playlist
                case 1:
                    if (level == 0) {
                        if (pos == 0) {
                            // add playlist page
                            List<Playlist> playlists = new ArrayList<Playlist>();
                            playlistAdapter = new PlaylistAdapter(inflater.getContext(), 1, playlists);
                            if (playlistAdapter != null) setListAdapter(playlistAdapter);
                            level = 1;
                        } else {
                            // existing playlist page
                            String playlistName = title.getText().toString();
                            List<Song> playlistSongs = db.getAllSongsInPlaylist(pos);
                            playlistAdapter = new PlaylistAdapter(inflater.getContext(), 1, playlistSongs, playlistName);
                            if (playlistAdapter != null) setListAdapter(playlistAdapter);

                            categoryTitle = playlistName;
                            level = 1;
                        }

                    }

                    else if (level == 1) {
                        if (pos == 1) { // add songs button

                        }
                    }
                    break;

                // Song select, doesn't actually do any thing here, since the song_listview.xml itself handles the onClick
                case 2:
                    break;

                // Select artist
                case 3:
                    if (level == 0) {
                        // get all songs from artist name
                        String artist = title.getText().toString();
                        List<Song> artistSongList = db.getAllSongsFromArtist(artist, 1);
                        // create and set adapter
                        songAdapter = new SongAdapter(view.getContext(), sectionNumber, 1, artistSongList, artist);
                        if (songAdapter != null) setListAdapter(songAdapter);

                        categoryTitle = artist;
                        level = 1;
                    }
                    // Songs inside an artist category
                    else if (level == 1) {
                        if (pos == 0) {
                            // if back button was pressed?
                        }
                    }
                    break;

                // Select album
                case 4:
                    if (level == 0) {
                        // get all songs from album name
                        String album = title.getText().toString();
                        List<Song> albumSongList = db.getAllSongsFromAlbum(album, 1);
                        // create and set adapter
                        songAdapter = new SongAdapter(view.getContext(), sectionNumber, 1, albumSongList, album);
                        if (songAdapter != null) setListAdapter(songAdapter);

                        categoryTitle = album;
                        level = 1;
                    }
                    // Songs inside an album category
                    else if (level == 1) {
                        if (pos == 0) {
                            // if back button was pressed?
                        }
                    }
                    break;

                case 5:
                    if (level == 0) {
                        // get all songs from artist name
                        String genre = title.getText().toString();
                        List<Song> genreSongList = db.getAllSongsFromGenre(genre, 1);
                        // create and set adapter
                        songAdapter = new SongAdapter(view.getContext(), sectionNumber, 1, genreSongList, genre);
                        if (songAdapter != null) setListAdapter(songAdapter);

                        categoryTitle = genre;
                        level = 1;
                    }
                    // Songs inside an genre category
                    else if (level == 1) {
                        if (pos == 0) {
                            // if back button was pressed?
                        }
                    }
                    break;
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_local_library, menu);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

            // Listener for search bar
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                // Function called on submit
                @Override
                public boolean onQueryTextSubmit(String query) {
                    SongList fragment = (SongList) getFragmentManager().findFragmentById(R.id.container);
                    fragment.search(currentPage, query);
                    return false;
                }
                // Function called while typing
                @Override
                public boolean onQueryTextChange(String searchQuery) {
                    SongList fragment = (SongList) getFragmentManager().findFragmentById(R.id.container);
                    fragment.search(currentPage, searchQuery);
                    return true;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);
        }


        // Search function called when typing in the search bar (magnifying glass)
        // wow i use switch cases a lot, our software profs would murder me
        public void search(int sectionNumber, String keyword) {
            DatabaseHandler db = new DatabaseHandler(inflater.getContext());
            List<Song> results;

            // Query and adapter differs for each section
            if (level == 0) {
                switch (sectionNumber) {
                    // Playlists
                    case 0:
                        List<Playlist> playlistResults = db.searchPlaylists(keyword);
                        if (playlistAdapter != null) {
                            playlistAdapter.updatePlaylistList(playlistResults);
                            playlistAdapter.notifyDataSetChanged();
                        }
                        break;

                    // Songs
                    case 1:
                        results = db.searchSongs("title", keyword);
                        if (songAdapterSongs != null) {
                            songAdapterSongs.updateSongList(results);
                            songAdapterSongs.notifyDataSetChanged();
                        }
                        break;

                    // Artists
                    case 2:
                        results = db.searchSongs("artist", keyword);
                        if (songAdapterArtists != null) {
                            songAdapterArtists.updateSongList(results);
                            songAdapterArtists.notifyDataSetChanged();
                        }
                        break;

                    // Albums
                    case 3:
                        results = db.searchSongs("album", keyword);
                        if (songAdapterAlbums != null) {
                            songAdapterAlbums.updateSongList(results);
                            songAdapterAlbums.notifyDataSetChanged();
                        }
                        break;

                    // Genres
                    case 4:
                        results = db.searchSongs("genre", keyword);
                        if (songAdapterGenres != null) {
                            songAdapterGenres.updateSongList(results);
                            songAdapterGenres.notifyDataSetChanged();
                        }
                        break;
                }
            }
            else if (level == 1) {
                String category = "";
                switch (sectionNumber) {
                    case 0:
                        category = "playlist";
                        results = db.searchSongsInCategory(category, categoryTitle, keyword);
                        playlistAdapter.updateSongsList(results);
                        playlistAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        category = "artist";
                        break;
                    case 3:
                        category = "album";
                        break;
                    case 4:
                        category = "genre";
                        break;
                }
                results = db.searchSongsInCategory(category, categoryTitle, keyword);
                if (songAdapter != null) {
                    songAdapter.updateSongList(results);
                    songAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
