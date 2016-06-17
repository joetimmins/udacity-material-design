package com.novoda.materialised;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.novoda.materialised.example.MessagePresenter;
import com.novoda.materialised.example.ToggleMessages;
import com.novoda.materialised.hackernews.StoryViewModel;
import com.novoda.materialised.hackernews.database.HackerNewsItemDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MessagePresenter messagePresenter = new MessagePresenter(new ToggleMessages("hello world!", "hello again!"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Toast.makeText(MainActivity.this, "This button does nothing", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }

        final Button helloWorld = (Button) findViewById(R.id.hello_world_button);
        if (helloWorld != null) {
            helloWorld.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                int finalRadius = (int) Math.hypot(view.getWidth() / 2, view.getHeight() / 2);
                                ((Button) view).setText(messagePresenter.currentMessage());
                                ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, 0, finalRadius).start();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        HackerNewsItemDatabase hnItems = FirebaseSingleton.INSTANCE.hackerNewsDatabase(this);
        StoryViewModel storyViewModel = hnItems.readItem(8863);

        TextView textView = (TextView) findViewById(R.id.firebase_text_view);
        if (textView != null) {
            textView.setText(storyViewModel.getTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
