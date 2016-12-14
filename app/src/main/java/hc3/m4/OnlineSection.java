package hc3.m4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.MediaController.MediaPlayerControl;

import java.util.ArrayList;
import java.util.Random;

public class OnlineSection extends AppCompatActivity implements MediaController.MediaPlayerControl {
    // Music service, to play music in the background ---------------------------
    private MusicService musicService;
    private MediaController musicController;
    private ArrayList<Song> songsToPlay;
    private int currentTrack;
    private boolean musicBound = false;
    private Intent playIntent;
    private boolean paused = false, playbackPaused = false;

    private TextView curSongTitle;
    private TextView curArtist;
    private ToggleButton playPauseButton;
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_online_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Music Controller set up --------------------------------------------
        songsToPlay = (ArrayList<Song>) getIntent().getSerializableExtra("playlistSongs");

        curSongTitle = (TextView) findViewById(R.id.songTitle);
        curArtist = (TextView) findViewById(R.id.artistName);
        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);

        currentTrack = getIntent().getIntExtra("currentTrackNumber", 0);
        songsToPlay = new ArrayList<Song>();
        ArrayList<Song> songsList = (ArrayList<Song>) getIntent().getSerializableExtra("playlistSongs");
        if ( songsList != null )
            songsToPlay.addAll( songsList );

        setController();
        // ---------------------------------------------------------------


        // Common navigation buttons along buttom
        Button btn_local = (Button) findViewById(R.id.btn_local);
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localLibrary = new Intent(OnlineSection.this, LocalLibrary.class);
                localLibrary.putExtra("playlistSize", songsToPlay.size());
                localLibrary.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
                localLibrary.putExtra("playlistSongs", songsToPlay);
                startActivity(localLibrary); // Opens Local Library
            }
        });

        // Online buttons
        Button btn_newreleases = (Button) findViewById(R.id.btn_newreleases);
        btn_newreleases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toNewRelease = new Intent(OnlineSection.this, NewReleasesRecommended.class);
                toNewRelease.putExtra("label", "New Releases");
                toNewRelease.putExtra("playlistSize", songsToPlay.size());
                toNewRelease.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
                toNewRelease.putExtra("playlistSongs", songsToPlay);
                startActivity(toNewRelease); // Opens New Releases Page
            }
        });
        Button btn_top100songs = (Button) findViewById(R.id.btn_top100songs);
        btn_top100songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent top100Songs = new Intent(OnlineSection.this, Top100Songs.class);
                top100Songs.putExtra("playlistSize", songsToPlay.size());
                top100Songs.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
                top100Songs.putExtra("playlistSongs", songsToPlay);
                startActivity(top100Songs); // Opens New Releases Page
            }
        });
        Button btn_recommended = (Button) findViewById(R.id.btn_recommended);
        btn_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRecommended = new Intent(OnlineSection.this, NewReleasesRecommended.class);
                toRecommended.putExtra("label", "Recommended");
                toRecommended.putExtra("playlistSize", songsToPlay.size());
                toRecommended.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
                toRecommended.putExtra("playlistSongs", songsToPlay);
                startActivity(toRecommended); // Opens Recommended for you
            }
        });

    }



    // Music Controller classes, to play/pause/control ------------------------
    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsToPlay);
            musicBound = true;

            updateCurTrack(false);

            //musicController.show(0);
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
        musicController.setAnchorView(findViewById(R.id.localLibraryRelativeLayout));
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
        //musicController.show(0);
    }
    public void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        //musicController.show(0);
    }


    // Function used to show play page
    // Current set to open new activity, but I think that's horrible practice. Might switch to play page = fragment
    public void openPlayPage() {
        Intent playPage = new Intent(OnlineSection.this, PlayPage.class);
        playPage.putExtra("playlistSize", songsToPlay.size());
        playPage.putExtra("currentTrackNumber", musicService.getCurrentTrackNumber());
        playPage.putExtra("playlistSongs", songsToPlay);
        startActivity(playPage); // Opens Play Page
    }
    public void updateCurTrack(boolean forSureIsPlaying) {
        curArtist.setText("By: " + musicService.getArtist());
        curSongTitle.setText(musicService.getSongTitle());

        if (forSureIsPlaying)
            playPauseButton.setChecked(true);
        else
            playPauseButton.setChecked(musicService.isPng());
    }
    // ----------------------------------------------------------------------------------------
}
