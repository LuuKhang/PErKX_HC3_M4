package hc3.m4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PlaylistAdapter extends BaseAdapter implements SectionIndexer {

    // Attempts at scrollbar ---------
    HashMap<String, Integer> mapIndex;
    String[] sections;
    //---------------------------------

    private Context context;
    private String playlistName;
    private List<Playlist> playlistList;
    private List<Song> songsInPlaylist;
    private int level = 0;

    public PlaylistAdapter(Context context, int level, List<Playlist> playlistLists) {
        this.context = context;
        this.playlistList = playlistLists;
        this.level = level;

        // Attempts at scrollbar --------------------------------------------------------------------------------
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < playlistList.size(); i++) {
            String song = playlistList.get(i).getName();

            String ch = song.substring(0, 1);
            ch = ch.toUpperCase(Locale.US);
            mapIndex.put(ch, i); // HashMap will prevent duplicates
        }

        Set<String> sectionLetters = mapIndex.keySet();
        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
        Log.d("sectionList", sectionList.toString());
        //----------------------------------------------------------------------------------------------------
    }

    public PlaylistAdapter(Context context, int level, List<Song> songs, String playlistName) {
        this.context = context;
        this.playlistName = playlistName;
        this.songsInPlaylist = songs;
        this.level = level;

        // Attempts at scrollbar --------------------------------------------------------------------------------
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < songsInPlaylist.size(); i++) {
            String song = songsInPlaylist.get(i).getTitle();

            String ch = song.substring(0, 1);
            ch = ch.toUpperCase(Locale.US);
            mapIndex.put(ch, i); // HashMap will prevent duplicates
        }

        Set<String> sectionLetters = mapIndex.keySet();
        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
        Log.d("sectionList", sectionList.toString());
        //----------------------------------------------------------------------------------------------------
    }

    // Function called to update the current list
    public void updatePlaylistList(List<Playlist> newPlaylists) {
        this.playlistList = newPlaylists;
    }
    public void updateSongsList(List<Song> newSongs) {
        this.songsInPlaylist = newSongs;
    }

    @Override
    public int getCount() {
        switch (level) {
            case 0:
                return playlistList.size();
            case 1:
                if (songsInPlaylist != null)
                    return songsInPlaylist.size();
                else
                    return 0;
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return playlistList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Song> getAllSongs() {
        return songsInPlaylist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView artist;
        ImageView artwork;

        switch (level) {
            // List showing all playlists
            case 0:
                // Lists all playlists
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_playlist, null);

                // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                artwork = (ImageView) convertView.findViewById(R.id.list_image);
                artwork.setImageResource(R.drawable.cross);

                title = (TextView) convertView.findViewById(R.id.title); // title
                title.setText(playlistList.get(position).getName());
                convertView.setTag(position);
                break;

            // Once clicked on a playlist, shows options and songs
            case 1:
                // First item shown is name row
                convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                artwork = (ImageView) convertView.findViewById(R.id.list_image);
                artwork.setImageResource(R.drawable.cross);

                convertView.setTag(position);

                title = (TextView) convertView.findViewById(R.id.title); // title
                artist = (TextView) convertView.findViewById(R.id.artist); // artist

                title.setText(songsInPlaylist.get(position).getTitle());
                artist.setText(songsInPlaylist.get(position).getArtist());
//                }
                break;
        }
        return convertView;
    }

    // Attempts at scrollbar --------------------------------------------------------------------------------
    public int getPositionForSection(int section) {
        Log.d("section", "" + section);
        return mapIndex.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        Log.d("position", "" + position);
        return 0;
    }

    public Object[] getSections() {
        return sections;
    }
    //-------------------------------------------------------------------------------------------------------
}