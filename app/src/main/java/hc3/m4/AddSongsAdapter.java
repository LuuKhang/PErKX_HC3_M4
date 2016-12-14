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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class AddSongsAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public List<Song> data;
    private int sectionNumber;
    private int level = 0;
    private String levelName;
    public int totalSelected = 0;

    public AddSongsAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
    }

    public AddSongsAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
    }

    public AddSongsAdapter(Context context, int sectionNumber, int level, List<Song> data, String name, int totalSelected) {
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
                        return data.size();
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
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_add_song_checkbox, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getTitle());
                        artist.setText(data.get(position).getArtist());

                        CheckBox cb = (CheckBox) convertView.findViewById(R.id.addcheckbox);
//                        cb.setChecked(true);
                        cb.setTag(data.get(position));

                        if (data.get(position).isSelected()) {
                            cb.setChecked(true);
//                            ImageButton downloadicon = (ImageButton) convertView.findViewById(R.id.download); // title
//                            downloadicon.setVisibility(View.INVISIBLE);
//                            TextView downloadCompleted = (TextView) convertView.findViewById(R.id.downloadcompleted);
//                            downloadCompleted.setVisibility(View.VISIBLE);
//                            downloadCompleted.setText("Downloaded");
                        }
                        break;
                    // artist
                    case 2:
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getArtist());
                        break;
                    // album
                    case 3:
                        convertView = LayoutInflater.from(context).inflate(R.layout.album_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getAlbum());
                        artist.setText(data.get(position).getArtist());
                        break;
                    // genre
                    case 4:
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getGenre());
                        break;
                    default:
                        break;
                }
                break;
            case 1: // for second view after selecting artist, album or genre
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_add_song_checkbox, null);

                title = (TextView) convertView.findViewById(R.id.title); // title
                artist = (TextView) convertView.findViewById(R.id.artist); // artist

                title.setText(data.get(position).getTitle());
                artist.setText(data.get(position).getArtist());

                CheckBox cb = (CheckBox) convertView.findViewById(R.id.addcheckbox);
                cb.setTag(data.get(position));

                if (data.get(position).isSelected()) {
                    cb.setChecked(true);
                }
                break;
            case 2: // for download songs
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