package de.greenrobot.eventperf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.SubscribeTag;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class TwoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoActivity.this,TestEventActivity.class));
            }
        });
        EventBus.getDefault().register(this);
    }
    public void onEventMainThread(TagEvent event){
        Log.e("Event","cccccccccccccccccccc");
    }
    @SubscribeTag("list")
    public void onEventMainThread(ArrayList<TagEvent> event){
        Log.e("Event","ArrayList<TagEvent> two");
    }
    public void onEventMainThread(List<TagEvent> event){
        Log.e("Event","List<TagEvent> two");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
