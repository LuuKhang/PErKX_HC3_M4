package hc3.m4;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class NewReleases extends AppCompatActivity {

    private NewReleases.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int currentPage = 0;


    // Music service, to play music in the background ---------------------------
    private MusicService musicService;
    private MediaController musicController;
    private ArrayList<Song> songsToPlay;
    private boolean musicBound = false;
    private Intent playIntent;
    private boolean paused = false, playbackPaused = false;

    private TextView curSongTitle;
    private TextView curArtist;
    private ToggleButton playPauseButton;
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_releases);

        // Initialize toolbar (top most portion of screen)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PErKX - New Releases"); // Set toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new NewReleases.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                NewReleases.SongList fragment = (NewReleases.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
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
        Button btn_local = (Button) findViewById(R.id.btn_local);
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewReleases.this, LocalLibrary.class)); // Opens Online Section
            }
        });

        // Database handler
        DatabaseHandler db = new DatabaseHandler(this);
        db.createDataBase();

        // Music Controller set up --------------------------------------------
//        curSongTitle = (TextView) findViewById(R.id.songTitle);
//        curArtist = (TextView) findViewById(R.id.artistName);
//        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
//
//        getSongList();
//        setController();
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
        NewReleases.SongList fragment = (NewReleases.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.update();
        }
    }

    public void downloadSong(View view) {
        RelativeLayout vwButton = (RelativeLayout) view.getParent();
        LinearLayout vwRow = (LinearLayout) vwButton.getParent();

        ImageButton downloadButton = (ImageButton) view.findViewById(R.id.download);
        TextView downloadCompleted = (TextView) vwButton.findViewById(R.id.downloadcompleted);

        downloadButton.setVisibility(View.INVISIBLE);
        downloadCompleted.setVisibility(View.VISIBLE);

        // update database from online to local
        TextView title = (TextView) vwRow.findViewById(R.id.title);
        TextView artist = (TextView) vwRow.findViewById(R.id.artist);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.downloadSong(title.getText().toString(), artist.getText().toString());
    }

    public void downloadMultipleSongs(View view) {
        NewReleases.SongList fragment = (NewReleases.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.selectSongsForDownload();
        }
        int px = (int) (50.0 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
        linearLayout.getLayoutParams().height = px;

        Button downloadMultiple = (Button) findViewById(R.id.downloadmultiple);
        Button downloadSelected = (Button) findViewById(R.id.downloadselected);
        downloadMultiple.setVisibility(View.GONE);
        downloadSelected.setVisibility(View.VISIBLE);
    }

    public void updateSelectedNumber(View view) {
        CheckBox cb = (CheckBox) view ;
        Song song = (Song) cb.getTag();
        song.setSelected(cb.isChecked());

        NewReleases.SongList fragment = (NewReleases.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        OnlineSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (OnlineSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 0;
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).isSelected()) {
                    count++;
                }
            }

            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " songs selected for download");
        }





    }

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
            return NewReleases.SongList.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4; // Number of tabs (pages)
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SONG";
                case 1:
                    return "ARTIST";
                case 2:
                    return "ALBUM";
                case 3:
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
        public static NewReleases.SongList newInstance(int sectionNumber) {
            NewReleases.SongList fragment = new NewReleases.SongList();

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

            // Creating an array adapter to store the list of items
            OnlineSongAdapter songAdapter = null;

            DatabaseHandler db = new DatabaseHandler(inflater.getContext());

            // ID number of the current section (label and id mapping may change)
            //  Playlist = 1
            //  Song = 2
            //  Artist = 3
            //  Album = 4
            //  Genre = 5
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                case 1:
                    List<Song> songs = db.getAllSongs(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, songs);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 2:
                    List<Song> artists = db.getAllArtists(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, artists);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 3:
                    List<Song> albums = db.getAllAlbums(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, albums);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 4:
                    List<Song> genres = db.getAllGenres(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, genres);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
            }

        }

        public void selectSongsForDownload() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            OnlineSongAdapter songAdapter = null;

            DatabaseHandler db = new DatabaseHandler(inflater.getContext());

            // ID number of the current section (label and id mapping may change)
            //  Playlist = 1
            //  Song = 2
            //  Artist = 3
            //  Album = 4
            //  Genre = 5
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                case 1:
                    List<Song> songs = db.getAllSongs(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, 2, songs, null);
                    // Setting the list adapter for the ListFragment
                    setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 2:
                    List<Song> artists = db.getAllArtists(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, artists);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 3:
                    List<Song> albums = db.getAllAlbums(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, albums);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 4:
                    List<Song> genres = db.getAllGenres(0);
                    songAdapter = new OnlineSongAdapter(inflater.getContext(), sectionNumber, genres);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
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
                case 1:
                    Toast.makeText(getActivity(), "SONG " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(getActivity(), "ARTIST " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (level == 0) {
                        // get all songs from artist name
                        String artist = title.getText().toString();
                        List<Song> artistSongList = db.getAllSongsFromArtist(artist, 1);
                        // create and set adapter
                        OnlineSongAdapter songAdapter = new OnlineSongAdapter(view.getContext(), sectionNumber, 1, artistSongList, artist);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;
                    } else if (level == 1) {
                        if (pos == 0) {
                            // if back button was pressed?
                        }
                    }
                    break;

                case 3:
                    Toast.makeText(getActivity(), "ALBUM " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (level == 0) {
                        // get all songs from album name
                        String album = title.getText().toString();
                        List<Song> albumSongList = db.getAllSongsFromAlbum(album, 1);
                        // create and set adapter
                        OnlineSongAdapter songAdapter = new OnlineSongAdapter(view.getContext(), sectionNumber, 1, albumSongList, album);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;
                    } else if (level == 1) {
                        if (pos == 0) {
                            // if back button was pressed?
                        }
                    }
                    break;

                case 4:
                    Toast.makeText(getActivity(), "GENRE " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (level == 0) {
                        // get all songs from artist name
                        String genre = title.getText().toString();
                        List<Song> genreSongList = db.getAllSongsFromGenre(genre, 1);
                        // create and set adapter
                        OnlineSongAdapter songAdapter = new OnlineSongAdapter(view.getContext(), sectionNumber, 1, genreSongList, genre);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;
                    } else if (level == 1) {
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
                    Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Function called while typing
                @Override
                public boolean onQueryTextChange(String searchQuery) {
                    Toast.makeText(getContext(), searchQuery, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);
        }
    }
}
