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

import hc3.m4.DatabaseHandler;
import hc3.m4.R;
import hc3.m4.Song;

public class SongAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<Song> data;
    private int sectionNumber;
    private int level = 0;
    private String levelName;

    // Attempts at scrollbar ---------
    HashMap<String, Integer> mapIndex;
    String[] sections;
//    List<Float> weights = new ArrayList<Float>();
    //---------------------------------

    public SongAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;

        getHashMap();

    }

    public SongAdapter(Context context, int sectionNumber, int level, List<Song> data, String name) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
        this.level = level;
        this.levelName = name;

        getHashMap();
    }

    public void getHashMap() {
        // Attempts at scrollbar --------------------------------------------------------------------------------
        mapIndex = new LinkedHashMap<String, Integer>();
        String song = "";

        if (level == 1) {
            for (int i = 0; i < data.size(); i++) {
                song = data.get(i).getTitle();

                String ch = song.substring(0, 1);
                ch = ch.toUpperCase(Locale.US);
                if (!mapIndex.containsKey(ch)) {
                    mapIndex.put(ch, i); // HashMap will prevent duplicates
                } else {
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                switch (sectionNumber) {
                    case 2:
                        song = data.get(i).getTitle();
                        break;
                    case 3:
                        song = data.get(i).getArtist();
                        break;
                    case 4:
                        song = data.get(i).getAlbum();
                        break;
                    case 5:
                        song = data.get(i).getGenre();
                        break;
                }

                String ch = song.substring(0, 1);
                ch = ch.toUpperCase(Locale.US);
                if (!mapIndex.containsKey(ch)) {
                    mapIndex.put(ch, i); // HashMap will prevent duplicates
                } else {
                }
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
    public void updateSongList(List<Song> newSongs) {
        this.data = newSongs;
    }

    @Override
    public int getCount() {
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    case 2:
                        return data.size();
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
                        return data.size();
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
        ImageView artwork;

        // Setting all values in listview
        switch (level) {
            case 0:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // song
                    case 2:
                        // song_listview: shows art, title, artist, and includes onClick()
                        convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        convertView.setTag(position);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getTitle());
                        artist.setText(data.get(position).getArtist());
                        break;
                    // artist
                    case 3:
                        // artist_listview: shows image, title, and does not includes onClick()
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getArtist());
                        break;
                    // album
                    case 4:
                        // album_listview exact same as song_listview, but does not have the onClick()
                        convertView = LayoutInflater.from(context).inflate(R.layout.album_listview, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getAlbum());
                        artist.setText(data.get(position).getArtist());
                        break;
                    // genre
                    case 5:
                        // Genre very similar to artist, so reuse
                        convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        title = (TextView) convertView.findViewById(R.id.title); // title

                        title.setText(data.get(position).getGenre());
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (sectionNumber) { // Switch case to populate list, depends on category of tab
                    // artist, album, genre
                    case 3:
                    case 4:
                    case 5:
                        convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);

                        // place holder image, can be replaced with real image (lol, we're never gonna get to that)
                        artwork = (ImageView) convertView.findViewById(R.id.list_image);
                        artwork.setImageResource(R.drawable.cross);

                        convertView.setTag(position);

                        title = (TextView) convertView.findViewById(R.id.title); // title
                        artist = (TextView) convertView.findViewById(R.id.artist); // artist

                        title.setText(data.get(position).getTitle());
                        artist.setText(data.get(position).getArtist());
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


    // Attempts at scrollbar --------------------------------------------------------------------------------
    public int getPositionForSection(int section) {
        Log.d("section", "" + section);
        return mapIndex.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        Log.d("position", "" + position);
        for(int i = sections.length - 1; i >= 0; i--) {
            if(position > mapIndex.get(sections[i]))
                return i;
        }
        return 0;
    }

    public Object[] getSections() {
        return sections;
    }

    public void displayIndex(LinearLayout indexLayout) {

        indexLayout.removeAllViews();

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        int count = 0;
        for (String index : indexList) {
            textView = (TextView) LayoutInflater.from(context).inflate(
                    R.layout.scroll_item, null);
            textView.setText(index);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
//                textView.setOnClickListener(this);
            indexLayout.addView(textView);
            count++;
        }
    }
    //-------------------------------------------------------------------------------------------------------
}