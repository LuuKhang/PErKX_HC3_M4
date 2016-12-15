package hc3.m4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class DeletePlaylistSongPage extends AppCompatActivity {

    private DeletePlaylistSongPage.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int currentPage = 0;

    public static String playlistName;
    public static List<Song> songslist;


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
        setContentView(R.layout.activity_delete_playlist_song);

        // Initialize toolbar (top most portion of screen)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PErKX - Local Library"); // Set toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DeletePlaylistSongPage.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
        numberDownload.setText("0 items selected to delete");

        TextView title = (TextView) findViewById(R.id.level1name);
        playlistName = getIntent().getStringExtra("PlaylistName");
        title.setText(playlistName);

        // Common navigation buttons along buttom
//        Button btn_local = (Button) findViewById(R.id.btn_local);
//        btn_local.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DeletePlaylistSongPage.this, LocalLibrary.class)); // Opens Online Section
//            }
//        });
//        Button btn_online = (Button) findViewById(R.id.btn_online);
//        btn_online.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DeletePlaylistSongPage.this, OnlineSection.class)); // Opens Online Section
//            }
//        });

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

    // go back to online page from new releases page
    public void backToLocalPage(View view) {
        finish();
    }

    // download all selected songs
    public void addSelectedSongs(View view) {

        // get database
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // get songAdapter and songList
        DeletePlaylistSongPage.SongList fragment = (DeletePlaylistSongPage.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        DeletePlaylistSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (DeletePlaylistSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.getAllSongs();
            int count = 1;
            Song currentSong;

            for (int i = 0; i < songList.size(); i++) {
                currentSong = songList.get(i);
                if (currentSong.isSelected()) { //if the song is selected
                    db.deletePlaylistSong(currentSong);
                    count++;
                }
            }
            songAdapter.setAllSongs(songList);
            fragment.setListAdapter(songAdapter);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("PlaylistName", playlistName);
            setResult(this.RESULT_OK, returnIntent);
            finish();
        }
    }

    // when only one checkbox is selected for downloading
    public void updateSelectedNumber(View view) {
        CheckBox cb = (CheckBox) view.findViewById(R.id.addcheckbox) ;
        Song song = (Song) cb.getTag();
        song.setSelected(cb.isChecked());

        DeletePlaylistSongPage.SongList fragment = (DeletePlaylistSongPage.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);

        DeletePlaylistSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (DeletePlaylistSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.getAllSongs();
            int count = 0;
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).isSelected()) {
                    count++;
                }
            }

//            songAdapter.setAllSongs(songList);

            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " items selected to delete");
        }
    }

    // check selected song if the row was clicked but not the checkbox
    public void checkSelectedSong(View view) {
        CheckBox cb = (CheckBox) view.findViewById(R.id.addcheckbox) ;
        Song song = (Song) cb.getTag();
        cb.setChecked(true);
        song.setSelected(cb.isChecked());

        updateSelectedNumber(view);
    }

    // When the select all checkbox is selected for downloading
    public void updateAll(View view) {
        CheckBox cb = (CheckBox) view ;
        boolean isCheck = cb.isChecked();

        DeletePlaylistSongPage.SongList fragment = (DeletePlaylistSongPage.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);

        DeletePlaylistSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (DeletePlaylistSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.getAllSongs();
            int count = 0;
            for (int i = 0; i < songList.size(); i++) {
                songList.get(i).setSelected(isCheck);
                if (songList.get(i).isSelected()) {
                    count++;
                }
            }

            songAdapter.setAllSongs(songList);
            fragment.setListAdapter(songAdapter);
            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " items selected to delete");
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
            return DeletePlaylistSongPage.SongList.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 1; // Number of tabs (pages)
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PLAYLIST";
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
        public static DeletePlaylistSongPage.SongList newInstance(int sectionNumber) {
            DeletePlaylistSongPage.SongList fragment = new DeletePlaylistSongPage.SongList();

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

        // return everything to level 0
        public void update() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            DeletePlaylistSongAdapter songAdapter = null;

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
                    // existing playlist page
//                        List<Song> playlistSongs = db.getAllSongsInPlaylist(pos+1);
                    List<Song> playlistSongs = db.getAllSongsInPlaylist(playlistName);
                    songAdapter = new DeletePlaylistSongAdapter(inflater.getContext(), 1, playlistSongs, playlistName);
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 1;

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
