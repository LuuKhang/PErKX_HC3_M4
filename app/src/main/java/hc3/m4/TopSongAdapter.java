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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TopSongAdapter extends BaseAdapter implements Filterable {

    // Attempts at scrollbar ---------
    HashMap<String, Integer> mapIndex;
    String[] sections;
    //---------------------------------

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
        getHashMap();
    }

    public TopSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
        getHashMap();
    }

    public TopSongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name, int totalSelected) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
        this.totalSelected = totalSelected;
        getHashMap();
    }

    public void getHashMap() {
        // Attempts at scrollbar --------------------------------------------------------------------------------
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < data.size(); i++) {
            if ((i%5) == 0) {
                mapIndex.put(String.valueOf(i+1), i); // HashMap will prevent duplicates
            }

//            if (!mapIndex.containsKey(ch)) {
//                mapIndex.put(ch, i); // HashMap will prevent duplicates
//            } else {
//            }
//            String ch = song.substring(0, 1);
//            ch = ch.toUpperCase(Locale.US);

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