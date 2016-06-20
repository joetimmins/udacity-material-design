package com.novoda.materialised;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.example.MessagePresenter;
import com.novoda.materialised.example.ToggleMessages;
import com.novoda.materialised.hackernews.StoryViewModel;
import com.novoda.materialised.hackernews.database.TopStories;
import com.novoda.materialised.hackernews.database.ValueCallback;

import java.util.List;

public final class MainActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityLayout = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mainActivityLayout.toolbar);

        mainActivityLayout.fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Toast.makeText(MainActivity.this, "This button does nothing", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        final MessagePresenter messagePresenter = new MessagePresenter(new ToggleMessages("hello world!", "hello again!"));

        mainActivityLayout.helloWorldButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            int finalRadius = (int) Math.hypot(view.getWidth() / 2, view.getHeight() / 2);
                            ((Button) view).setText(messagePresenter.currentMessage());
                            ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, 0, finalRadius).start();
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        final FirebaseSingleton firebaseSingleton = FirebaseSingleton.INSTANCE;
        TopStories topStories = firebaseSingleton.hackerNewsTopStories();
        topStories.readAll(new ValueCallback<List<Integer>>() {
            @Override
            public void onValueRetrieved(List<Integer> value) {
                firebaseSingleton.hackerNewsItems(MainActivity.this).readItem(value.get(0), new ValueCallback<StoryViewModel>() {
                    @Override
                    public void onValueRetrieved(StoryViewModel value) {
                        mainActivityLayout.firebaseTextView.setText(value.getTitle());
                    }
                });
            }
        });
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
