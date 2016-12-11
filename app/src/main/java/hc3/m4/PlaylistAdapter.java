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

    public PlaylistAdapter(Context context, List<Playlist> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size()+1;
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

        if (position == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.create_playlist_listview, null);

        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.artist_listview, null);

            title = (TextView) convertView.findViewById(R.id.title); // title

            title.setText(data.get(position-1).getName());
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