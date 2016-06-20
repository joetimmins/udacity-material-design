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

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.example.MessagePresenter;
import com.novoda.materialised.example.ToggleMessages;
import com.novoda.materialised.firebase.FirebaseItemsDatabase;
import com.novoda.materialised.firebase.FirebaseSingleton;
import com.novoda.materialised.firebase.FirebaseTopStoriesDatabase;
import com.novoda.materialised.hackernews.items.StoryViewModel;
import com.novoda.materialised.hackernews.topstories.StoryView;
import com.novoda.materialised.hackernews.topstories.TopStoriesPresenter;

public final class MainActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityLayout;
    private TopStoriesPresenter topStoriesPresenter;

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
                            ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, 0, finalRadius).start();
                        }
                        ((Button) view).setText(messagePresenter.currentMessage());
                    }
                }
        );

        StoryView storyView = new StoryView() {
            @Override
            public void updateWith(StoryViewModel storyViewModel) {
                mainActivityLayout.firebaseTextView.setText(storyViewModel.getTitle());
            }
        };

        FirebaseDatabase firebaseDatabase = FirebaseSingleton.INSTANCE.getFirebaseDatabase(this);
        topStoriesPresenter = new TopStoriesPresenter(new FirebaseTopStoriesDatabase(firebaseDatabase), new FirebaseItemsDatabase(firebaseDatabase), storyView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        topStoriesPresenter.startPresenting();
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
