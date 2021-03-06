package hc3.m4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DeletePlaylistAdapter extends BaseAdapter implements SectionIndexer {

    // Attempts at scrollbar ---------
    HashMap<String, Integer> mapIndex;
    String[] sections;
    //---------------------------------

    private Context context;
    private String playlistName;
    public List<Playlist> playlistList;
    private List<Song> songsInPlaylist;
    private int level = 0;

    public DeletePlaylistAdapter(Context context, int level, List<Playlist> playlistLists) {
        this.context = context;
        this.playlistList = playlistLists;
        this.level = level;
        getHashMap();
    }

    public void getHashMap() {
        // Attempts at scrollbar --------------------------------------------------------------------------------
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < playlistList.size(); i++) {
            String song = playlistList.get(i).getName();

            String ch = song.substring(0, 1);
            ch = ch.toUpperCase(Locale.US);
            if (!mapIndex.containsKey(ch)) {
                mapIndex.put(ch, i); // HashMap will prevent duplicates
            } else {
            }
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
        switch (level) {
            // List showing all playlists
            case 0:
                // Lists all playlists
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_delete_playlist_checkbox, null);
                title = (TextView) convertView.findViewById(R.id.title); // title
                title.setText(playlistList.get(position).getName());

                CheckBox cb = (CheckBox) convertView.findViewById(R.id.addcheckbox);
                cb.setTag(playlistList.get(position));

                if (playlistList.get(position).isSelected()) {
                    cb.setChecked(true);
                }
                break;

            // Once clicked on a playlist, shows options and songs
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


    public void displayIndex(LinearLayout indexLayout) {

        indexLayout.removeAllViews();

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) LayoutInflater.from(context).inflate(
                    R.layout.scroll_item, null);
            textView.setText(index);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
//                textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }
    //-------------------------------------------------------------------------------------------------------
}