package hc3.m4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

import hc3.m4.coverflow.CoverFlow;
import hc3.m4.coverflow.PagerContainer;

public class PlayPage extends AppCompatActivity implements MediaPlayerControl {
    // Music service, to play music in the background ---------------------------
    private MusicService musicService;
    private MediaController musicController;
    private ArrayList<Song> songsToPlay;
    private boolean musicBound = false;
    private Intent playIntent;
    private boolean paused = false, playbackPaused = false;
    private int currentTrack;

    private int curTimeInt;
    private TextView songTitle;
    private TextView artistName;
    private TextView songCurTime;
    private TextView songTotalTime;
    private ToggleButton playPauseButton;
    private SeekBar seekbar;

    private Handler updateProgressThread = new Handler();
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

        // Thread that continously runs to update the progress bar
        PlayPage.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicService != null && isPlaying()){
                    seekbar.setMax(musicService.getDur());
                    seekbar.setProgress(musicService.getCurrentProgress());
                    songCurTime.setText(musicService.getStringProgress());

                    // This is pretty bad... updating the labels everytime, but on song switch, im not sure how to set the listener
                    updateCurTrack(false);
                }
                updateProgressThread.postDelayed(this, 1000);
            }
        });

        songTitle = (TextView) findViewById(R.id.songTitle);
        artistName = (TextView) findViewById(R.id.artistName);
        songCurTime = (TextView) findViewById(R.id.songCurTime);
        songTotalTime = (TextView) findViewById(R.id.songTotalTime);
        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seek(progresValue);
                    songCurTime.setText(musicService.getStringProgress());
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("test", "touched down");
                // may change scrub speed, if we have time
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("test", "released touch");
            }
        });
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
                    updateCurTrack(true);
                }
                else if (position < currentTrack) {
                    musicService.playPrev();
                    currentTrack = position;
                    updateCurTrack(true);
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
            // Depending on input position, can load index of an array to get the images of albums
            ImageView view = new ImageView(PlayPage.this);
            view.setImageResource(R.drawable.cross);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!paused) {
                        paused = true;
                        playPauseButton.setChecked(false);
                        pause();
                    }
                    else {
                        paused = false;
                        playPauseButton.setChecked(true);
                        start();
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
        songsToPlay.add(new Song("almost_easy", "Avenged Sevefold", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("master_of_puppets", "Metallica", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("insomnia", "Kamelot", "SongAlbum1", "SongArt1", "SongGenre1"));
        songsToPlay.add(new Song("lets_see_it", "We Are Scientists", "SongAlbum1", "SongArt1", "SongGenre1"));
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

            //musicController.show(0);
            paused = !musicService.isPng();
            updateCurTrack(false);
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
        musicController = new MediaController(this);
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
    public void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        updateCurTrack(true);
        //musicController.show(0);

        currentTrack = musicService.getCurrentTrackNumber();
        pager.setCurrentItem(currentTrack);
    }
    public void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        updateCurTrack(true);
        //musicController.show(0);

        currentTrack = musicService.getCurrentTrackNumber();
        pager.setCurrentItem(currentTrack);
    }
    public void fastForward() {
        if (!paused) {
            int newPos = musicService.getCurrentProgress() + 10000; // move forward 10 seconds
            int maxPos = musicService.getDur();
            if (newPos > maxPos)
                musicService.seek(maxPos);
            else
                musicService.seek(newPos);
        }
    }
    public void rewind() {
        if (!paused) {
            int newPos = musicService.getCurrentProgress() - 10000; // move back 10 seconds
            int minPos = 0;
            if (newPos < minPos)
                musicService.seek(minPos);
            else
                musicService.seek(newPos);
        }
    }

    public void updateCurTrack(final boolean forSureIsPlaying) {
        // This is probably terrible coding practice
        // But if we don't run it with a slight delay, the threads clash (playing music and updating labels)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curTimeInt = musicService.getCurrentProgress();
                artistName.setText("By: " + musicService.getArtist());
                songTitle.setText(musicService.getSongTitle());
                songTotalTime.setText(musicService.getStringDuration());
                songCurTime.setText(musicService.getStringProgress());

                currentTrack = musicService.getCurrentTrackNumber();
                pager.setCurrentItem(currentTrack);

                if (forSureIsPlaying) {
                    playPauseButton.setChecked(true);
                    paused = false;
                }
                else
                    playPauseButton.setChecked(musicService.isPng());
            }
        }, 100);
    }
    // ----------------------------------------------------------------------------------------



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(PlayPage.this, LocalLibrary.class)); // Opens Local Library
                //finish(); // Functions similar to back button
                // finish blows everything up... clearly I implemented the music player badly, but too late to fix... my bad
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
