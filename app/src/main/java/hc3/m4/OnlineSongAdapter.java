package hc3.m4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OnlineSongAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public List<Song> data;
    private int sectionNumber;
    private int level = 0;
    private String levelName;
    public int totalSelected = 0;

    public OnlineSongAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
    }

    public OnlineSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
    }

    public OnlineSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name, int totalSelected) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
        this.totalSelected = totalSelected;
    }

    @Override
    public int getCount() {
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return data.size();
                    default:
                        return 0;
                }
            case 1:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    case 2:
                    case 3:
                    case 4:
                        return data.size()+2;
                    default:
                        return 0;
                }
            case 2:
                return data.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView artist;

        // Setting all values in listview
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // song
                    case 1:
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_song, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getTitle());
                        artist.setText(data.get(position).getArtist());

                        if (data.get(position).getLocal() == 1) {
                            ImageView downloadicon = (ImageView) convertView.findViewById(R.id.download); // title
                            downloadicon.setImageResource(0);
                        }
                        break;
                    // artist
                    case 2:
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_artist, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getArtist());
                        break;
                    // album
                    case 3:
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_song, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getAlbum());
                        artist.setText(data.get(position).getArtist());
                        break;
                    // genre
                    case 4:
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_artist, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getGenre());
                        break;
                    default:
                        break;
                }
                break;
            case 1: // for second view after selecting artist, album or genre
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // artist
                    case 2:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position-2).getTitle());
                            artist.setText(data.get(position-2).getArtist());
                        }
                        break;
                    // album
                    case 3:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position-2).getTitle());
                            artist.setText(data.get(position-2).getArtist());
                        }
                        break;
                    // genre
                    case 4:
                        if (position == 0) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.albumname_back_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title

                            title.setText(levelName);
                        } else if (position == 1) {
                            convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                        } else {
                            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                            title = (TextView) convertView.findViewById(R.id.title); // title
                            artist = (TextView) convertView.findViewById(R.id.artist); // artist

                            title.setText(data.get(position-2).getTitle());
                            artist.setText(data.get(position-2).getArtist());
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 2: // for download songs
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_song_checkbox, null);
                title = (TextView) convertView.findViewById(R.id.title); // title
                artist = (TextView) convertView.findViewById(R.id.artist); // artist
                CheckBox downloadcheckbox = (CheckBox) convertView.findViewById(R.id.downloadcheckbox); // download checkbox
                downloadcheckbox.setTag(data.get(position));

                title.setText(data.get(position).getTitle());
                artist.setText(data.get(position).getArtist());
                downloadcheckbox.setChecked(data.get(position).isSelected());


                break;
            default:
                break;
        }




        return convertView;
    }

    @Override
    public Filter getFilter() {
        //test
        Log.d("TEST", "test");
        return null;
    }
}