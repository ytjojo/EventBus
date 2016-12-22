package de.greenrobot.eventperf;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class TestEventActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activiy_testevent);
        findViewById(R.id.tv_Last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Event","post");
                EventBus.getDefault().post(new TagEvent(),true);
            }
        });
        findViewById(R.id.tv_All).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Event","post");
                ArrayList<TagEvent> tagEvents =new ArrayList<TagEvent>();
                tagEvents.add(new TagEvent());
                EventBus.getDefault().post("list",tagEvents,3000);
            }
        });
        findViewById(R.id.tv_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Event","post");
                EventBus.getDefault().post("hello",new TagEvent());
            }
        });
    }
    public void onEventMainThread(TagEvent event){
        Log.e("Event","BBBBBBBBBBBBBBBB");
    }
    public void onEventMainThread(ArrayList<TagEvent> event){
        Log.e("Event","ArrayList<TagEvent>");
    }
    @Subscribe("list")
    public void onEventMainThread(List<TagEvent> event){
        Log.e("Event","List<TagEvent> ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
