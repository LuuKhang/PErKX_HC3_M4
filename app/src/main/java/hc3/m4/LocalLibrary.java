package hc3.m4;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import android.widget.Button;
import android.widget.ArrayAdapter;
import android.support.v4.app.ListFragment;
import android.widget.ListView;
import android.widget.Toast;


public class LocalLibrary extends AppCompatActivity {

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // Common navigation buttons along buttom
        Button btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocalLibrary.this, PlayPage.class)); // Opens Play Page
            }
        });
        Button btn_online = (Button) findViewById(R.id.btn_online);
        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocalLibrary.this, OnlineSection.class)); // Opens Online Section
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_library, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        // Listener for search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Function called on submit
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(LocalLibrary.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }
            // Function called while typing
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Toast.makeText(LocalLibrary.this, searchQuery, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
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
        // Define the items in each tab's list
        String[] playlists = new String[] {
                "Playlist A",
                "Playlist B",
                "Playlist C",
                "Playlist D",
                "Playlist E"
        };
        String[] songs = new String[]{
                "Song 1",
                "Song 2",
                "Song 3",
                "Song 4",
                "Song 5",
                "Song 6",
                "Song 7",
                "Song 8",
                "Song 9",
                "Song 10",
                "Song 11",
                "Song 12",
                "Song 13",
                "Song 14"
        };
        String[] artists = new String[]{
                "Artist 1",
                "Artist 2",
                "Artist 3",
                "Artist 4",
                "Artist 5",
                "Artist 6",
                "Artist 7",
                "Artist 8",
                "Artist 9",
                "Artist 10",
                "Artist 11",
                "Artist 12",
                "Artist 13",
                "Artist 14"
        };
        String[] albums = new String[]{
                "Album 1",
                "Album 2",
                "Album 3",
                "Album 4",
                "Album 5",
                "Album 6",
                "Album 7",
                "Album 8",
                "Album 9",
                "Album 10",
                "Album 11",
                "Album 12",
                "Album 13",
                "Album 14"
        };
        String[] genres = new String[]{
                "Genre A",
                "Genre B",
                "Genre C",
                "Genre D",
                "Genre E",
                "Genre F"
        };

        // Key defined, for id of current tab
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            // Creating an array adapter to store the list of items
            ArrayAdapter<String> adapter = null;

            // ID number of the current section (label and id mapping may change)
            //  Playlist = 1
            //  Song = 2
            //  Artist = 3
            //  Album = 4
            //  Genre = 5
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                case 1:
                    adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, playlists);
                    break;

                case 2:
                    adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, songs);
                    break;

                case 3:
                    adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, artists);
                    break;

                case 4:
                    adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, albums);
                    break;

                case 5:
                    adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, genres);
                    break;
            }

            // Setting the list adapter for the ListFragment
            if (adapter != null) setListAdapter(adapter);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        // Function called when a tab's list view item is clicked
        @Override
        public void onListItemClick(ListView listview, View view, int pos, long id) {
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) { // Depending current tab, different action
                case 1:
                    Toast.makeText(getActivity(), "PLAYLIST " + (String)listview.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(getActivity(), "SONG " + (String)listview.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    Toast.makeText(getActivity(), "ARTIST " + (String)listview.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Toast.makeText(getActivity(), "ALBUM " + (String)listview.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    break;

                case 5:
                    Toast.makeText(getActivity(), "GENRE " + (String)listview.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
