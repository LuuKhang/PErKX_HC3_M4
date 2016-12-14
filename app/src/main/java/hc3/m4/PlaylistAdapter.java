package hc3.m4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

    private Context context;
    private String playlistName;
    private List<Playlist> playlistList;
    private List<Song> songsInPlaylist;
    private int level = 0;

    public PlaylistAdapter(Context context, int level, List<Playlist> playlistLists) {
        this.context = context;
        this.playlistList = playlistLists;
        this.level = level;
    }

    public PlaylistAdapter(Context context, int level, List<Song> songs, String playlistName) {
        this.context = context;
        this.playlistName = playlistName;
        this.songsInPlaylist = songs;
        this.level = level;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_playlist, null);
                title = (TextView) convertView.findViewById(R.id.title); // title
                title.setText(playlistList.get(position).getName());
                convertView.setTag(position);
                break;

            // Once clicked on a playlist, shows options and songs
            case 1:
                // First item shown is name row
//                if (position == 0) {
//                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_playlistname, null);
//                    title = (TextView) convertView.findViewById(R.id.title);
//                    title.setText(playlistName);
//                }
                // Second, "Add Songs" button
//                else if (position == 1) {
//                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_addsongs, null);
//                }
                // Third. "Shuffle All" button
//                else if (position == 2) {
//                    convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
//                }
                // All other positions list the songs in the current playlist
//                else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

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
}