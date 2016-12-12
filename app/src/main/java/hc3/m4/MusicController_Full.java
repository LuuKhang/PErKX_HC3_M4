package hc3.m4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class MusicController_Full extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_music_controller_full, container, false);
        this.rootView = rootView;

        ImageButton prevButton = (ImageButton) rootView.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Previous");

                ((PlayPage)getActivity()).playPrev();
            }
        });

        ImageButton rewindButton = (ImageButton) rootView.findViewById(R.id.rewindButton);
        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Rewind");

                ((PlayPage)getActivity()).rewind();
            }
        });

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Next");

                ((PlayPage)getActivity()).playNext();
            }
        });

        ImageButton forwardButton = (ImageButton) rootView.findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Fast forward");

                ((PlayPage)getActivity()).fastForward();
            }
        });

        final ToggleButton playPauseButton = (ToggleButton) rootView.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPauseButton.isChecked()){
                    Log.d("Test", "Play");

                    ((PlayPage)getActivity()).start();
                }
                else {
                    Log.d("Test", "Pause");

                    ((PlayPage)getActivity()).pause();
                }
            }
        });

        SeekBar musicSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        return rootView;
    }
}