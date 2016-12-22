package de.greenrobot.eventperf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * This activity gets the information from the activity before, sets up the test and starts the test. After it watchs
 * after that, if a test is finished. When a test is finished, the activity appends it on the textview analyse. If all
 * test are finished, it cancels the timer.
 */
public class TestRunnerActivity extends Activity {

    private TestRunner testRunner;
    private EventBus controlBus;
    private TextView textViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtests);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        controlBus = new EventBus();
        controlBus.register(this);
        EventBus.getDefault().register(this);
        findViewById(R.id.nextActvity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestRunnerActivity.this,TwoActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (testRunner == null) {
            TestParams testParams = (TestParams) getIntent().getSerializableExtra("params");
            testRunner = new TestRunner(getApplicationContext(), testParams, controlBus);

            if (testParams.getTestNumber() == 1) {
                textViewResult.append("Events: " + testParams.getEventCount() + "\n");
            }
            textViewResult.append("Subscribers: " + testParams.getSubscriberCount() + "\n\n");
            testRunner.start();
        }
    }

    public void onEventMainThread(TestFinishedEvent event) {
        Test test = event.test;
        String text = "<b>" + test.getDisplayName() + "</b><br/>" + //
                test.getPrimaryResultMicros() + " micro seconds<br/>" + //
                ((int) test.getPrimaryResultRate()) + "/s<br/>";
        if (test.getOtherTestResults() != null) {
            text += test.getOtherTestResults();
        }
        text += "<br/>----------------<br/>";
        textViewResult.append(Html.fromHtml(text));
        if (event.isLastEvent) {
            findViewById(R.id.buttonCancel).setVisibility(View.GONE);
            findViewById(R.id.textViewTestRunning).setVisibility(View.GONE);
            findViewById(R.id.buttonKillProcess).setVisibility(View.VISIBLE);
        }
    }
    @Subscribe("hello")
    public void onEventMainThread(TagEvent event) {
        Log.e("Event","AAAAAAAAAAAAAAAAAAaaa");
    }

    public void onClickCancel(View view) {
        // Cancel asap
        if (testRunner != null) {
            testRunner.cancel();
            testRunner = null;
        }
        finish();
    }

    public void onClickKillProcess(View view) {
        Process.killProcess(Process.myPid());
    }

    public void onDestroy() {
        if (testRunner != null) {
            testRunner.cancel();
        }
        controlBus.unregister(this);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
