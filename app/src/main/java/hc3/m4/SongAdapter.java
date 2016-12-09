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

public class SongAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Song> data;
    private int sectionNumber;

    public SongAdapter(Context context, int sectionNumber, List<Song> data) {
        this.context = context;
        this.data = data;
        this.sectionNumber = sectionNumber;
    }

    @Override
    public int getCount() {
        switch (sectionNumber) { // Switch case to populate list, depends on category of tab
            case 1:
                return 3;
            case 2:
                return data.size()+1;
            case 3:
            case 4:
            case 5:
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title); // title
        TextView artist = (TextView) convertView.findViewById(R.id.artist); // artist

        // Setting all values in listview
        switch (sectionNumber) { // Switch case to populate list, depends on category of tab
            case 1:
                break;
            case 2:
                if (position == 0) {
                    float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (25 * scale + 0.5f);

                    // Gets layout params of ImageView to resize
                    ImageView layout = (ImageView) convertView.findViewById(R.id.list_image);
                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    params.height = pixels;
                    params.width = pixels;
                    layout.setLayoutParams(params);

                    // Gets layout params of TextView to resize
                    TextView artist_text = (TextView) convertView.findViewById(R.id.artist);
                    artist_text.setTextSize(0);

                    title.setText("Shuffle All");
                    artist.setText("");
                } else {
                    float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (50 * scale + 0.5f);

                    // Gets layout params of ImageView to resize
                    ImageView layout = (ImageView) convertView.findViewById(R.id.list_image);
                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    params.height = pixels;
                    params.width = pixels;
                    layout.setLayoutParams(params);

                    // Gets layout params of TextView to resize
                    TextView artist_text = (TextView) convertView.findViewById(R.id.artist);
                    artist_text.setTextSize(15);

                    title.setText(data.get(position-1).getTitle());
                    artist.setText(data.get(position-1).getArtist());
                }
                break;
            case 3:
                // Gets layout params of TextView to resize
                TextView artist_text = (TextView) convertView.findViewById(R.id.artist);
                artist_text.setTextSize(0);

                title.setText(data.get(position).getArtist());
                artist.setText("");
                break;
            case 4:
//                float scale = context.getResources().getDisplayMetrics().density;
//                int pixels = (int) (50 * scale + 0.5f);
//
//                // Gets layout params of ImageView to resize
//                ImageView layout = (ImageView) convertView.findViewById(R.id.list_image);
//                ViewGroup.LayoutParams params = layout.getLayoutParams();
//                params.height = pixels;
//                params.width = pixels;
//                layout.setLayoutParams(params);

                // Gets layout params of TextView to resize
//                TextView artist_text = (TextView) convertView.findViewById(R.id.artist);
//                artist_text.setTextSize(0);

                title.setText(data.get(position).getAlbum());
                artist.setText(data.get(position).getArtist());
                break;
            case 5:
                // Gets layout params of TextView to resize
                artist_text = (TextView) convertView.findViewById(R.id.artist);
                artist_text.setTextSize(0);

                title.setText(data.get(position).getGenre());
                artist.setText("");
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