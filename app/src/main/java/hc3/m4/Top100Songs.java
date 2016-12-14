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

public class Top100Songs extends AppCompatActivity {

    private Top100Songs.SectionsPagerAdapter mSectionsPagerAdapter;

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
        setContentView(R.layout.activity_top100_songs);

        // Initialize toolbar (top most portion of screen)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PErKX - Top 100 Songs"); // Set toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new Top100Songs.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                if (fragment != null) {
                    fragment.update();
                }
                currentPage = position;

                // disable select all row
                int px = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
                linearLayout.getLayoutParams().height = px;
                linearLayout.setVisibility(View.VISIBLE);

                linearLayout = (LinearLayout) findViewById(R.id.level1);
                linearLayout.getLayoutParams().height = px;
                linearLayout.setVisibility(View.VISIBLE);

                // enable cancel button
                ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
                ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelbutton);
                ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
                backButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                backlevel0Button.setVisibility(View.GONE);
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
                startActivity(new Intent(Top100Songs.this, LocalLibrary.class)); // Opens Online Section
            }
        });
        Button btn_online = (Button) findViewById(R.id.btn_online);
        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Top100Songs.this, OnlineSection.class)); // Opens Online Section
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

    // go back to level 0 listview
    public void backButton(View view) {
        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.update();
        }

        // disable level1 row
        int px = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
        linearLayout.getLayoutParams().height = px;
        linearLayout.setVisibility(View.VISIBLE);

        linearLayout = (LinearLayout) findViewById(R.id.level1);
        linearLayout.getLayoutParams().height = px;

        // enable back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelbutton);
        ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
        backButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
        backlevel0Button.setVisibility(View.GONE);
    }

    // go back to online page from new releases page
    public void backToOnlinePage(View view) {
        startActivity(new Intent(Top100Songs.this, OnlineSection.class));
    }

    // download single song
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

    // download all selected songs
    public void downloadSelectedSongs(View view) {
//        RelativeLayout vwButton = (RelativeLayout) view.getParent();
//        LinearLayout vwRow = (LinearLayout) vwButton.getParent();
//
//        TextView downloadCompleted = (TextView) vwButton.findViewById(R.id.downloadcompleted);
//        downloadCompleted.setVisibility(View.VISIBLE);

        // get database
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // get songAdapter and songList
        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        TopSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (TopSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 0;
            Song currentSong;
            for (int i = 0; i < songList.size(); i++) {
                currentSong = songList.get(i);
                if (currentSong.getLocal() == 0) { // if the song is not local
                    if (currentSong.isSelected()) { //if the song is selected
                        db.downloadSong(currentSong.getTitle(), currentSong.getArtist());
                        currentSong.setLocal(1);
                        currentSong.setSelected(false);
                    }
                }
            }
            songAdapter.data = songList;
            fragment.setListAdapter(songAdapter);

            Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), "SONG " + title.getText().toString(), Toast.LENGTH_SHORT).show();

            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText("0 songs selected for download");
        }
    }

    // when download multiple songs is selected
    public void downloadMultipleSongs(View view) {
        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.selectSongsForDownload(); // load listview with checkboxes
        }

        // enable select all row
        int px = (int) (60.0 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
        linearLayout.getLayoutParams().height = px;
        linearLayout.setVisibility(View.VISIBLE);

        linearLayout = (LinearLayout) findViewById(R.id.level1);
        linearLayout.getLayoutParams().height = px;
        linearLayout.setVisibility(View.INVISIBLE);


        Button downloadMultiple = (Button) findViewById(R.id.downloadmultiple);
        Button downloadSelected = (Button) findViewById(R.id.downloadselected);
        downloadMultiple.setVisibility(View.GONE);
        downloadSelected.setVisibility(View.VISIBLE);

        // enable cancel button
        ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelbutton);
        ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
        backButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        backlevel0Button.setVisibility(View.GONE);
    }

    // when only one checkbox is selected for downloading
    public void updateSelectedNumber(View view) {
        CheckBox cb = (CheckBox) view ;
        Song song = (Song) cb.getTag();
        song.setSelected(cb.isChecked());

        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        TopSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (TopSongAdapter) fragment.getListAdapter();
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

    // When the select all checkbox is selected for downloading
    public void updateAll(View view) {
        CheckBox cb = (CheckBox) view ;

        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        TopSongAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (TopSongAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 0;
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getLocal() == 0) {
                    songList.get(i).setSelected(cb.isChecked());
                    if (songList.get(i).isSelected()) {
                        count++;
                    }
                }
            }
            songAdapter.data = songList;
            fragment.setListAdapter(songAdapter);
            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " songs selected for download");
        }
    }

    // when multiple download is cancelled
    public void cancelDownloadMultiple(View view) {
        Top100Songs.SongList fragment = (Top100Songs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
//            fragment.update(); // load listview with checkboxes
            fragment.update();

        }

        // if it is the artist, album, genre pages, return to level 1
        if (currentPage > 0) {

            // disable select all row
//            int px = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
//            linearLayout.getLayoutParams().height = px;
            linearLayout.setVisibility(View.INVISIBLE);

            linearLayout = (LinearLayout) findViewById(R.id.level1);
//            linearLayout.getLayoutParams().height = px;
            linearLayout.setVisibility(View.VISIBLE);

            Button downloadMultiple = (Button) findViewById(R.id.downloadmultiple);
            Button downloadSelected = (Button) findViewById(R.id.downloadselected);
            downloadMultiple.setVisibility(View.VISIBLE);
            downloadSelected.setVisibility(View.GONE);

            // enable cancel button
            ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
            ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelbutton);
            ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
            backButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            backlevel0Button.setVisibility(View.VISIBLE);

        } else { // if it is the song page, return to level 0
            // disable select all row
            int px = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
            linearLayout.getLayoutParams().height = px;
            linearLayout.setVisibility(View.VISIBLE);

            linearLayout = (LinearLayout) findViewById(R.id.level1);
            linearLayout.getLayoutParams().height = px;
            linearLayout.setVisibility(View.VISIBLE);

            Button downloadMultiple = (Button) findViewById(R.id.downloadmultiple);
            Button downloadSelected = (Button) findViewById(R.id.downloadselected);
            downloadMultiple.setVisibility(View.VISIBLE);
            downloadSelected.setVisibility(View.GONE);

            // enable cancel button
            ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
            ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelbutton);
            ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
            backButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.GONE);
            backlevel0Button.setVisibility(View.GONE);
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
            return Top100Songs.SongList.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 1; // Number of tabs (pages)
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SONG";
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
        public static Top100Songs.SongList newInstance(int sectionNumber) {
            Top100Songs.SongList fragment = new Top100Songs.SongList();

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
            Button downloadMultiple = (Button) getActivity().findViewById(R.id.downloadmultiple);
            downloadMultiple.setVisibility(View.VISIBLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        // return everything to level 0
        public void update() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            TopSongAdapter songAdapter = null;
            Button downloadMultiple = (Button) getActivity().findViewById(R.id.downloadmultiple);

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
                    List<Song> songs = db.getAllSongsByID(0);
                    songAdapter = new TopSongAdapter(inflater.getContext(), sectionNumber, songs);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    downloadMultiple.setVisibility(View.VISIBLE);
                    break;
            }

        }

        // make checkbox lists for all song lists
        public void selectSongsForDownload() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            TopSongAdapter songAdapter = null;
            TextView level1name = (TextView) getActivity().findViewById(R.id.level1name);
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
                    songAdapter = new TopSongAdapter(inflater.getContext(), sectionNumber, 2, songs, null);
                    // Setting the list adapter for the ListFragment
                    setListAdapter(songAdapter);
                    level = 2;
                    break;
            }

        }


        // Attempts at scrollbar ----------------------------------------------------------------
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            this.getListView().setFastScrollEnabled(true);
            this.getListView().setFastScrollAlwaysVisible(true);
        }
        //--------------------------------------------------------------------------------------


        // Function called when a tab's list view item is clicked
        @Override
        public void onListItemClick(ListView listview, View view, int pos, long id) {
            DatabaseHandler db = new DatabaseHandler(view.getContext());
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView level1name = (TextView) getActivity().findViewById(R.id.level1name);
            Button downloadMultiple = (Button) getActivity().findViewById(R.id.downloadmultiple);

            switch (sectionNumber) { // Depending current tab, different action
                case 1:
                    Toast.makeText(getActivity(), "SONG " + title.getText().toString(), Toast.LENGTH_SHORT).show();
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
