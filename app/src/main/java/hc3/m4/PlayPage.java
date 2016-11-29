package hc3.m4;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PlayPage extends AppCompatActivity {

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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                finish(); // Functions similar to back button
            }
        });
    }

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
