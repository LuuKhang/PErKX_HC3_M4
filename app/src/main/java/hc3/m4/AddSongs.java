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

public class AddSongs extends AppCompatActivity {

    private AddSongs.SectionsPagerAdapter mSectionsPagerAdapter;

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
        setContentView(R.layout.activity_add_songs);

        // Initialize toolbar (top most portion of screen)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PErKX - Local Library"); // Set toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new AddSongs.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                if (fragment != null) {
                    fragment.update();
                }
                currentPage = position;

                if (currentPage == 0) {
                    // enable select all row
                    int px = (int) (60.0 * Resources.getSystem().getDisplayMetrics().density);
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
                    linearLayout.getLayoutParams().height = px;

                    linearLayout = (LinearLayout) findViewById(R.id.level1);
                    linearLayout.getLayoutParams().height = 0;

                    // enable cancel button
                    ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
                    ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
                    backButton.setVisibility(View.VISIBLE);
                    backlevel0Button.setVisibility(View.GONE);
                } else {
                    // disable select all row
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
                    linearLayout.getLayoutParams().height = 0;

                    linearLayout = (LinearLayout) findViewById(R.id.level1);
                    linearLayout.getLayoutParams().height = 0;

                    // enable cancel button
                    ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
                    ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
                    backButton.setVisibility(View.VISIBLE);
                    backlevel0Button.setVisibility(View.GONE);
                }

                LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);
                AddSongsAdapter song = (AddSongsAdapter) fragment.getListAdapter();
                song.displayIndex(indexLayout);

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
                onPageSelected(position);
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

        TextView title = (TextView) findViewById(R.id.title);
        playlistName = getIntent().getStringExtra("PlaylistName");
        title.setText(playlistName);
        TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
        numberDownload.setText("0 songs selected to add to " + playlistName);

        // Common navigation buttons along buttom
        Button btn_local = (Button) findViewById(R.id.btn_local);
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSongs.this, LocalLibrary.class)); // Opens Online Section
            }
        });
        Button btn_online = (Button) findViewById(R.id.btn_online);
        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSongs.this, OnlineSection.class)); // Opens Online Section
            }
        });

        // Database handler
        DatabaseHandler db = new DatabaseHandler(this);
        db.createDataBase();

        songslist = db.getAllSongsByID(0);
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
        AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
            fragment.update();
        }

        // disable level1 row
        int px = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header2);
        linearLayout.getLayoutParams().height = px;

        linearLayout = (LinearLayout) findViewById(R.id.level1);
        linearLayout.getLayoutParams().height = px;

        // enable back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
        ImageButton backlevel0Button = (ImageButton) findViewById(R.id.backlevel0button);
        backButton.setVisibility(View.VISIBLE);
        backlevel0Button.setVisibility(View.GONE);
    }

    // go back to online page from new releases page
    public void backToLocalPage(View view) {
        finish();
    }

    // download all selected songs
    public void addSelectedSongs(View view) {
//        RelativeLayout vwButton = (RelativeLayout) view.getParent();
//        LinearLayout vwRow = (LinearLayout) vwButton.getParent();
//
//        TextView downloadCompleted = (TextView) vwButton.findViewById(R.id.downloadcompleted);
//        downloadCompleted.setVisibility(View.VISIBLE);

        // get database
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // get playlist id
        int pid = db.getPlaylistID(playlistName);

//        Toast.makeText(this, playlistName, Toast.LENGTH_LONG).show();
//        Toast.makeText(this, String.valueOf(pid), Toast.LENGTH_LONG).show();

        // get songAdapter and songList
        AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        AddSongsAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (AddSongsAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 1;
            Song currentSong;

            for (int i = 0; i < songList.size(); i++) {
                currentSong = songList.get(i);
                if (currentSong.isSelected()) { //if the song is selected
                    db.addSongToPlaylist(pid, (int) currentSong.getID(), count);
                    count++;
                }
            }
            songAdapter.data = songList;
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

        songslist.get((int) song.getID()-1).setSelected(cb.isChecked());

        AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        AddSongsAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (AddSongsAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 0;
            for (int i = 0; i < songslist.size(); i++) {
                if (songslist.get(i).isSelected()) {
                    count++;
                }
            }

            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " songs selected to add to " + playlistName);
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

        AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        AddSongsAdapter songAdapter;
        if (fragment != null) {
            songAdapter = (AddSongsAdapter) fragment.getListAdapter();
            List<Song> songList = songAdapter.data;
            int count = 0;
            for (int i = 0; i < songList.size(); i++) {
                songList.get(i).setSelected(isCheck);
                songslist.get((int) songList.get(i).getID()-1).setSelected(isCheck);
            }
            for (int i = 0; i < songslist.size(); i++) {
                if (songslist.get(i).isSelected()) {
                    count++;
                }
            }
            songAdapter.data = songList;
            fragment.setListAdapter(songAdapter);
            TextView numberDownload = (TextView) findViewById(R.id.numberdownload);
            numberDownload.setText(count + " songs selected to add to " + playlistName);
        }
    }

    // when multiple download is cancelled
    public void cancelDownloadMultiple(View view) {
        AddSongs.SongList fragment = (AddSongs.SongList) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage);
        if (fragment != null) {
//            fragment.update(); // load listview with checkboxes
            fragment.updateSongList();

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
            return AddSongs.SongList.newInstance(position + 1);
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
        public static AddSongs.SongList newInstance(int sectionNumber) {
            AddSongs.SongList fragment = new AddSongs.SongList();

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

            LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
            AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
            song.displayIndex(indexLayout);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        // return everything to level 0
        public void update() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            AddSongsAdapter songAdapter = null;

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
                    List<Song> songs = db.getAllSongs(1);
                    for (int i = 0; i < songs.size(); i++) {
                        songs.get(i).setSelected(songslist.get((int) songs.get(i).getID()-1).isSelected());
                    }
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, songs);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 2:
                    List<Song> artists = db.getAllArtists(1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, artists);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 3:
                    List<Song> albums = db.getAllAlbums(1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, albums);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 4:
                    List<Song> genres = db.getAllGenres(1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, genres);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 0;
                    break;
            }

//            LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
//            AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
//            song.displayIndex(indexLayout);

        }

        // go to song list view for each page (level 0 for song, level 1 for other pages)
        public void updateSongList() {
            setHasOptionsMenu(true);

            // Creating an array adapter to store the list of items
            AddSongsAdapter songAdapter = null;
            TextView level1name = (TextView) getActivity().findViewById(R.id.level1name);
            DatabaseHandler db = new DatabaseHandler(inflater.getContext());

            LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
            AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
            song.displayIndex(indexLayout);

            // ID number of the current section (label and id mapping may change)
            //  Playlist = 1
            //  Song = 2
            //  Artist = 3
            //  Album = 4
            //  Genre = 5
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                case 1:
                    List<Song> songs = db.getAllSongs(1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, songs);
                    // Setting the list adapter for the ListFragment
                    setListAdapter(songAdapter);
                    level = 0;
                    break;
                case 2:
                    String artist = level1name.getText().toString();
                    // create and set adapter
                    List<Song> artistSongList = db.getAllSongsFromArtist(artist, 1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, 1, artistSongList, null);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 1;
                    break;
                case 3:
                    String album = level1name.getText().toString();
                    List<Song> albumSongList = db.getAllSongsFromAlbum(album, 1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, 1, albumSongList, null);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 1;
                    break;
                case 4:
                    String genre = level1name.getText().toString();
                    List<Song> genreSongList = db.getAllSongsFromGenre(genre, 1);
                    songAdapter = new AddSongsAdapter(inflater.getContext(), sectionNumber, 1, genreSongList, null);
                    // Setting the list adapter for the ListFragment
                    if (songAdapter != null) setListAdapter(songAdapter);
                    level = 1;
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
                case 2:
                    Toast.makeText(getActivity(), "ARTIST " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (level == 0) {
                        // get all songs from artist name
                        String artist = title.getText().toString();
                        List<Song> artistSongList = db.getAllSongsFromArtist(artist, 1);
                        level1name.setText(artist);

                        for (int i = 0; i < artistSongList.size(); i++) {
                            artistSongList.get(i).setSelected(songslist.get((int) artistSongList.get(i).getID()-1).isSelected());
                        }

                        // create and set adapter
                        AddSongsAdapter songAdapter = new AddSongsAdapter(view.getContext(), sectionNumber, 1, artistSongList, artist);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;

                        // enable level1 row
                        int px = (int) (60.0 * Resources.getSystem().getDisplayMetrics().density);
                        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.header2);
                        linearLayout.getLayoutParams().height = px;

                        linearLayout = (LinearLayout) getActivity().findViewById(R.id.level1);
                        linearLayout.getLayoutParams().height = px;

                        // enable back button
                        ImageButton backButton = (ImageButton) getActivity().findViewById(R.id.backbutton);
                        ImageButton backlevel0Button = (ImageButton) getActivity().findViewById(R.id.backlevel0button);
                        backButton.setVisibility(View.GONE);
                        backlevel0Button.setVisibility(View.VISIBLE);

                        LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
                        AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
                        song.displayIndex(indexLayout);
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
                        level1name.setText(album);

                        for (int i = 0; i < albumSongList.size(); i++) {
                            albumSongList.get(i).setSelected(songslist.get((int) albumSongList.get(i).getID()-1).isSelected());
                        }

                        // create and set adapter
                        AddSongsAdapter songAdapter = new AddSongsAdapter(view.getContext(), sectionNumber, 1, albumSongList, album);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;

                        // enable level1 row
                        int px = (int) (60.0 * Resources.getSystem().getDisplayMetrics().density);
                        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.header2);
                        linearLayout.getLayoutParams().height = px;

                        linearLayout = (LinearLayout) getActivity().findViewById(R.id.level1);
                        linearLayout.getLayoutParams().height = px;

                        // enable back button
                        ImageButton backButton = (ImageButton) getActivity().findViewById(R.id.backbutton);
                        ImageButton backlevel0Button = (ImageButton) getActivity().findViewById(R.id.backlevel0button);
                        backButton.setVisibility(View.GONE);
                        backlevel0Button.setVisibility(View.VISIBLE);

                        LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
                        AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
                        song.displayIndex(indexLayout);
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
                        level1name.setText(genre);

                        for (int i = 0; i < genreSongList.size(); i++) {
                            genreSongList.get(i).setSelected(songslist.get((int) genreSongList.get(i).getID()-1).isSelected());
                        }

                        // create and set adapter
                        AddSongsAdapter songAdapter = new AddSongsAdapter(view.getContext(), sectionNumber, 1, genreSongList, genre);
                        if (songAdapter != null) setListAdapter(songAdapter);
                        level = 1;

                        // enable level1 row
                        int px = (int) (60.0 * Resources.getSystem().getDisplayMetrics().density);
                        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.header2);
                        linearLayout.getLayoutParams().height = px;

                        linearLayout = (LinearLayout) getActivity().findViewById(R.id.level1);
                        linearLayout.getLayoutParams().height = px;

                        // enable back button
                        ImageButton backButton = (ImageButton) getActivity().findViewById(R.id.backbutton);
                        ImageButton backlevel0Button = (ImageButton) getActivity().findViewById(R.id.backlevel0button);
                        backButton.setVisibility(View.GONE);
                        backlevel0Button.setVisibility(View.VISIBLE);

                        LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);
                        AddSongsAdapter song = (AddSongsAdapter) getListAdapter();
                        song.displayIndex(indexLayout);
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
