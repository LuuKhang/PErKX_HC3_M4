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


                if (getContext() instanceof LocalLibrary) {
                    ((LocalLibrary)getActivity()).openPlayPage();
                }
                else if (getContext() instanceof NewReleasesRecommended) {
                    ((NewReleasesRecommended)getActivity()).openPlayPage();
                }
                else if (getContext() instanceof OnlineSection) {
                    ((OnlineSection)getActivity()).openPlayPage();
                }
                else if (getContext() instanceof Top100Songs) {
                    ((Top100Songs)getActivity()).openPlayPage();
                }
            }
        });

        ImageButton prevButton = (ImageButton) rootView.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Previous");

                if (getContext() instanceof LocalLibrary) {
                    ((LocalLibrary)getActivity()).playPrev();
                    ((LocalLibrary)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof NewReleasesRecommended) {
                    ((NewReleasesRecommended)getActivity()).playPrev();
                    ((NewReleasesRecommended)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof OnlineSection) {
                    ((OnlineSection)getActivity()).playPrev();
                    ((OnlineSection)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof Top100Songs) {
                    ((Top100Songs)getActivity()).playPrev();
                    ((Top100Songs)getActivity()).updateCurTrack(true);
                }
            }
        });

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Next");

                if (getContext() instanceof LocalLibrary) {
                    ((LocalLibrary)getActivity()).playNext();
                    ((LocalLibrary)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof NewReleasesRecommended) {
                    ((NewReleasesRecommended)getActivity()).playNext();
                    ((NewReleasesRecommended)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof OnlineSection) {
                    ((OnlineSection)getActivity()).playNext();
                    ((OnlineSection)getActivity()).updateCurTrack(true);
                }
                else if (getContext() instanceof Top100Songs) {
                    ((Top100Songs)getActivity()).playNext();
                    ((Top100Songs)getActivity()).updateCurTrack(true);
                }
            }
        });

        final ToggleButton playPauseButton = (ToggleButton) rootView.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPauseButton.isChecked()){
                    Log.d("Test", "Play");

                    if (getContext() instanceof LocalLibrary) {
                        ((LocalLibrary)getActivity()).start();
                    }
                    else if (getContext() instanceof NewReleasesRecommended) {
                        ((NewReleasesRecommended)getActivity()).start();
                    }
                    else if (getContext() instanceof OnlineSection) {
                        ((OnlineSection)getActivity()).start();
                    }
                    else if (getContext() instanceof Top100Songs) {
                        ((Top100Songs)getActivity()).start();
                    }
                }
                else {
                    Log.d("Test", "Pause");

                    if (getContext() instanceof LocalLibrary) {
                        ((LocalLibrary)getActivity()).pause();
                    }
                    else if (getContext() instanceof NewReleasesRecommended) {
                        ((NewReleasesRecommended)getActivity()).pause();
                    }
                    else if (getContext() instanceof OnlineSection) {
                        ((OnlineSection)getActivity()).pause();
                    }
                    else if (getContext() instanceof Top100Songs) {
                        ((Top100Songs)getActivity()).pause();
                    }
                }
            }
        });

        return rootView;
    }
}