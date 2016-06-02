package com.novoda.udacitymaterialdesignapp;

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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean isHelloWorldShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                                Button pressed = (Button) view;
                                String text = isHelloWorldShowing ? "hello again!" : "hello world!";
                                isHelloWorldShowing = !isHelloWorldShowing;
                                pressed.setText(text);
                                ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, 0, finalRadius).start();
                            }
                        }
                    });
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
