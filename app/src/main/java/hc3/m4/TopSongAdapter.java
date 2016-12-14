package hc3.m4;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TopSongAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public List<Song> data;
    private int sectionNumber;
    private int level = 0;
    private String levelName;
    public int totalSelected = 0;

    public TopSongAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
    }

    public TopSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
    }

    public TopSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name, int totalSelected) {
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

    public List<Song> getAll() {
        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView artist;
        TextView number;
        ImageView artwork;

        // Setting all values in listview
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // song
                    case 1:
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_top_song, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        convertView.setTag(position);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist
                        number = (TextView) convertView.findViewById(R.id.number);

                        title.setText(data.get(position).getTitle());
                        artist.setText(data.get(position).getArtist());
                        number.setText((position+1) + ")");

                        if (data.get(position).getLocal() == 1) {
                            ImageButton downloadicon = (ImageButton) convertView.findViewById(R.id.download); // title
                            downloadicon.setVisibility(View.INVISIBLE);
                            TextView downloadCompleted = (TextView) convertView.findViewById(R.id.downloadcompleted);
                            downloadCompleted.setVisibility(View.VISIBLE);
                            downloadCompleted.setText("Downloaded");
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 2: // for download songs
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_download_top_song_checkbox, null);
                title = (TextView) convertView.findViewById(R.id.title); // title
                artist = (TextView) convertView.findViewById(R.id.artist); // artist
                number = (TextView) convertView.findViewById(R.id.number);

                TextView downloadCompleted = (TextView) convertView.findViewById(R.id.downloadcompleted);
                CheckBox downloadcheckbox = (CheckBox) convertView.findViewById(R.id.downloadcheckbox); // download checkbox

                downloadcheckbox.setTag(data.get(position));

                title.setText(data.get(position).getTitle());
                artist.setText(data.get(position).getArtist());
                number.setText((position+1) + ")");

                if (data.get(position).getLocal() == 1) { // if the song is local
                    downloadcheckbox.setBackgroundResource(R.drawable.download_blocked);
                    downloadCompleted.setText("Downloaded");
                    downloadcheckbox.setOnClickListener(null);
                } else {
                    downloadcheckbox.setChecked(data.get(position).isSelected());
                }
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