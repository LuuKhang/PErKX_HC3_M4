package hc3.m4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import android.widget.MediaController.MediaPlayerControl;
import java.util.ArrayList;

import hc3.m4.coverflow.CoverFlow;
import hc3.m4.coverflow.LinkagePager;
import hc3.m4.coverflow.PageItemClickListener;
import hc3.m4.coverflow.PagerContainer;

public class PlayPage extends AppCompatActivity implements MediaPlayerControl {
    // Music service, to play music in the background ---------------------------
    private MusicService musicService;
    private MusicController musicController;
    private ArrayList<Song> songsToPlay;
    private boolean musicBound = false;
    private Intent playIntent;
    private boolean paused = false, playbackPaused = false;
    private int currentTrack;
    // -------------------------------------------------------------------------

    // For Coverflow ------------------------------------------------------------
    ViewPager pager;
    // ----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_play_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Adds back button to top left of header bar

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayPage.this, LocalLibrary.class)); // Opens Local Library
                //finish(); // Functions similar to back button
            }
        });



        // Music Controller set up --------------------------------------------
        getSongList();
        setController();
        // ---------------------------------------------------------------



        // Coverflow experiment, not sure if it'll work ------------------------------------------------
        PagerContainer container = (PagerContainer) findViewById(R.id.pager_container);
        pager = container.getViewPager();
        pager.setAdapter(new MyPagerAdapter());
        pager.setClipChildren(false);
        pager.setOffscreenPageLimit(5);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position > currentTrack) {
                    musicService.playNext();
                    currentTrack = position;
                }
                else if (position < currentTrack) {
                    musicService.playPrev();
                    currentTrack = position;
                }
            }

            // Called when we move a pic (but not actually change songs)
            // I honestly don't know what the dif between the two functions are, but both are required as part of the implementation
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        currentTrack = getIntent().getIntExtra("currentTrackNumber", 0);
        pager.setCurrentItem(currentTrack);

//        container.setPageItemClickListener(new PageItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                pause();
//                Toast.makeText(PlayPage.this,"position:" + position,Toast.LENGTH_SHORT).show();
//            }
//        });

        boolean showTransformer = getIntent().getBooleanExtra("showTransformer",false);
        if(showTransformer){
            new CoverFlow.Builder()
                    .with(pager)
                    .scale(0f)
                    .pagerMargin(0f)
                    .spaceSize(0f)
                    .build();
        }else{
            pager.setPageMargin(30);
        }
    }
    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            TextView view = new TextView(PlayPage.this);
//            view.setText("Item "+position);
//            view.setGravity(Gravity.CENTER);
//            view.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50));
//            container.addView(view);
//            return view;

            // Depending on input position, can load index of an array to get the images of albums
            ImageView view = new ImageView(PlayPage.this);
            view.setImageResource(R.drawable.cross);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paused) {
                        paused = false;
                        start();
                    }
                    else {
                        paused = true;
                        pause();
                    }
                }
            });

            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
        @Override
        public int getCount() {
            return getIntent().getIntExtra("playlistSize", 0);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
        // Function to set the width of each image (a "page"), default is 1 = full width of screen
        @Override
        public float getPageWidth(int position) {
            return(1f);
        }
    }
    // ----------------------------------------------------------------------------------



    // Music Controller classes, to play/pause/control ------------------------
    public void getSongList() {
        songsToPlay = new ArrayList<Song>();
        // Hard coded dummy list, this list isn't actually displayed, only for the music musicController
        songsToPlay.add(new Song("almost_easy", "SongArtist1", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("master_of_puppets", "SongArtist1", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("insomnia", "SongArtist1", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("lets_see_it", "SongArtist1", "SongAlbum1", "SongArt1", "SongGenre1"));
    }
    public void songSelected(View view){
        //musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setSong(0);
        musicService.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        musicController.show(0);
    }
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsToPlay);
            musicBound = true;

            musicController.show(0);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    private void setController() {
        musicController = new MusicController(this);
        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.playPageRelativeLayout));
        musicController.setEnabled(true);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
    }
    @Override
    public void start() {
        musicService.go();
    }
    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getDur();
        else return 0;
    }
    @Override
    public int getCurrentPosition() {
        return musicService.getPosn();
    }
    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }
    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPng();
        return false;
    }
    @Override
    public int getBufferPercentage() {
        return 0;
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() {
        return true;
    }
    @Override
    public boolean canSeekForward() {
        return true;
    }
    @Override
    public int getAudioSessionId() {
        return 0;
    }
    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
        System.exit(0);
    }
    //play next
    private void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        musicController.show(0);

        currentTrack = musicService.getCurrentTrackNumber();
        pager.setCurrentItem(currentTrack);
    }
    private void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        musicController.show(0);

        currentTrack = musicService.getCurrentTrackNumber();
        pager.setCurrentItem(currentTrack);
    }
    // ----------------------------------------------------------------------------------------



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // For detecting swipe
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            // On touch, records x position
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            // On finger release, record how far finger moved and calculate swipe
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                // Swipe right
                if (deltaX > MIN_DISTANCE) {
                    Toast.makeText(this, "Swipe RIGHT", Toast.LENGTH_SHORT).show();
                }

                // Swipe left
                else if (deltaX < MIN_DISTANCE * -1) {
                    Toast.makeText(this, "Swipe LEFT", Toast.LENGTH_SHORT).show();
                }

                // Tap
                else {
                    Toast.makeText(this, "TAP", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onTouchEvent(event);
    }
}
