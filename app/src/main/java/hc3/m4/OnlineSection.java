package hc3.m4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class OnlineSection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_online_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Common navigation buttons along buttom
        Button btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineSection.this, PlayPage.class)); // Opens Play Page
            }
        });
        Button btn_local = (Button) findViewById(R.id.btn_local);
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineSection.this, LocalLibrary.class)); // Opens Local Library
            }
        });

        // Online buttons
        Button btn_newreleases = (Button) findViewById(R.id.btn_newreleases);
        btn_newreleases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineSection.this, NewReleases.class)); // Opens New Releases Page
            }
        });
        Button btn_top100songs = (Button) findViewById(R.id.btn_top100songs);
        btn_top100songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineSection.this, Top100Songs.class)); // Opens New Releases Page
            }
        });
        Button btn_recommended = (Button) findViewById(R.id.btn_recommended);
        btn_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineSection.this, RecommendedForYou.class)); // Opens New Releases Page
            }
        });

    }

}
