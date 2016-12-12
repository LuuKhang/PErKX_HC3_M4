package hc3.m4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class MusicController_Simple extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_music_controller_simple, container, false);
        this.rootView = rootView;

        RelativeLayout musicControllerLayout = (RelativeLayout) rootView.findViewById(R.id.musicControllerLayout);
        musicControllerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Open play page");

                ((LocalLibrary)getActivity()).openPlayPage();
            }
        });

        ImageButton prevButton = (ImageButton) rootView.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Previous");

                ((LocalLibrary)getActivity()).playPrev();
                ((LocalLibrary)getActivity()).updateCurTrack(true);
            }
        });

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Next");

                ((LocalLibrary)getActivity()).playNext();
                ((LocalLibrary)getActivity()).updateCurTrack(true);
            }
        });

        final ToggleButton playPauseButton = (ToggleButton) rootView.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPauseButton.isChecked()){
                    Log.d("Test", "Play");

                    ((LocalLibrary)getActivity()).start();
                }
                else {
                    Log.d("Test", "Pause");

                    ((LocalLibrary)getActivity()).pause();
                }
            }
        });

        return rootView;
    }
}