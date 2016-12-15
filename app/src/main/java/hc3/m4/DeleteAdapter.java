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

public class DeleteAdapter extends BaseAdapter implements SectionIndexer {

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

    public DeleteAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        getHashMap();
    }

    public DeleteAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;
        getHashMap();
    }

    public DeleteAdapter(Context context, int sectionNumber, int level, List<Song> data, String name, int totalSelected) {
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

        String song = "";
        for (int i = 0; i < data.size(); i++) {
            switch (level) {
                case 0:
                    switch (sectionNumber) {
                        case 1:
                            song = data.get(i).getTitle();
                            break;
                        case 2:
                            song = data.get(i).getArtist();
                            break;
                        case 3:
                            song = data.get(i).getAlbum();
                            break;
                        case 4:
                            song = data.get(i).getGenre();
                            break;
                    }
                    break;
                case 1:
                    song = data.get(i).getTitle();
                    break;
            }

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
                        convertView = LayoutInflater.from(context).inflate(R.layout.listview_delete_song_checkbox, null);

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