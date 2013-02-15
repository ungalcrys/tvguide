package ro.ungalcrys.portro;

import ro.ungalcrys.portro.task.RefereshChannelsTask;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ListView;

public class TvChannelsActivity extends Activity {
    public static final String TAG = "========";

    private static LayoutInflater inflater = null;
    private static TvChannelsActivity instance;

    private ListView listView;
    private Channel[] channels;
    private ChannelsAdapter adapter;

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        instance = this;

        handler = new Handler();
        inflater = getLayoutInflater();

        listView = (ListView) findViewById(R.id.list);

        new RefereshChannelsTask(this).start();// execute(new Void[] {});
    }

    public static ListView getListView() {
        return instance.listView;
    }

    public static Handler getHandler() {
        return instance.handler;
    }

    public static LayoutInflater getInflater() {
        return inflater;
    }

    public static void setAdapter(final Channel[] channels) {
        instance.channels = channels;
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.adapter = new ChannelsAdapter();
                instance.listView.setAdapter(instance.adapter);
            }
        });
    }

    public static Channel[] getChannels() {
        return instance.channels;
    }
    
    public static ChannelsAdapter getAdapter() {
        return instance.adapter;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
