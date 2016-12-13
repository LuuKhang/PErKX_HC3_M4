package hc3.m4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

public class PlaylistAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Playlist> data;
    private int level = 0;

    public PlaylistAdapter(Context context, int level, List<Playlist> data) {
        this.context = context;
        this.data = data;
        this.level = level;
    }

    @Override
    public int getCount() {
        switch (level) {
            case 0:
                return data.size()+1;
            case 1:
                return data.size()+3;
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
        switch (level) {
            case 0:
                if (position == 0) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.create_playlist_listview, null);

                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

                    title = (TextView) convertView.findViewById(R.id.title); // title

                    title.setText(data.get(position-1).getName());
                }
                break;
            case 1:
                if (position == 0) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_playlistname, null);
                } else if (position == 1) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_addsongs, null);
                } else if (position == 2) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.shuffleall_listview, null);
                } else {

                }
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