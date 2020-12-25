package com.example.aegisproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.common.AegisAccessibilityDelegate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityNodeInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               fab.performAccessibilityAction(AccessibilityNodeInfo.ACTION_CLICK, null);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       fab.performAccessibilityAction(AccessibilityNodeInfo.ACTION_CLICK, null);
                   }
               }, 2000);
           }
       }, 2000);
        fab.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("luo", String.format("x = %f, y = %f", event.getX(), event.getY()));
            }
            return false;
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            @Aegis(interval = 2000L, toastMsg = "你被禁用了！！！")
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //fab.setAccessibilityDelegate(new AegisAccessibilityDelegate());
        fab.setOnClickListener(this);
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

    @Override
    @Aegis(interval = 2000L, toastMsg = "禁用了！！！",strategy = Aegis.DisableStrategy.Fibonacci)
    public void onClick(View v) {
//        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }
}