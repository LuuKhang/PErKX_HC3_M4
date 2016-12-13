package hc3.m4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hc3.m4.DatabaseHandler;
import hc3.m4.R;
import hc3.m4.Song;

public class SongAdapter extends BaseAdapter {

    private Context context;
    private List<Song> data;
    private int sectionNumber;
    private int level = 0;
    private String levelName;

    public SongAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
    }

    public SongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
    }

    // Function called to update the current list
    public void updateSongList(List<Song> newSongs) {
        this.data = newSongs;
    }

    @Override
    public int getCount() {
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    case 2:
                        return data.size()+1;
                    case 3:
                    case 4:
                    case 5:
                        return data.size();
                    default:
                        return 0;
                }
            case 1:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    case 3:
                    case 4:
                    case 5:
                        return data.size()+2;
                    default:
                        return 0;
                }
            default:
                return 0;
        }

    }


    @Override
    public Object getItem(int position) {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Song> getAll() {
        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView artist;

        // Setting all values in listview
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // song
                    case 2:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);

                        } else {
                            // song_listview: shows art, title, artist, and includes onClick()
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            convertView.setTag(position - 1);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position - 1).getTitle());
                            artist.setText(data.get(position - 1).getArtist());
                        }
                        break;
                    // artist
                    case 3:
                        // artist_listview: shows image, title, and does not includes onClick()
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getArtist());
                        break;
                    // album
                    case 4:
                        // album_listview exact same as song_listview, but does not have the onClick()
                        convertView = LayoutInflater.from(context).inflate(R.layout.album_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getAlbum());
                        artist.setText(data.get(position).getArtist());
                        break;
                    // genre
                    case 5:
                        // Genre very similar to artist, so reuse
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getGenre());
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // artist
                    case 3:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            convertView.setTag(position - 2);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position - 2).getTitle());
                            artist.setText(data.get(position - 2).getArtist());
                        }
                        break;
                    // album
                    case 4:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            convertView.setTag(position - 2);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position - 2).getTitle());
                            artist.setText(data.get(position - 2).getArtist());
                        }
                        break;
                    // genre
                    case 5:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            convertView.setTag(position - 2);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position - 2).getTitle());
                            artist.setText(data.get(position - 2).getArtist());
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }


        return convertView;
    }
}