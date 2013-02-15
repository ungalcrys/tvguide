package ro.ungalcrys.portro.task;

import java.util.ArrayList;

import ro.ungalcrys.portro.Channel;
import ro.ungalcrys.portro.TvChannelsActivity;
import ro.ungalcrys.portro.db.StationsOpenHelper;
import ro.ungalcrys.portro.html.Parser;
import android.app.ProgressDialog;
import android.content.Context;

public class RefereshChannelsTask extends Thread {
    private final Context context;

    // used because of anonymous classes
    private ProgressDialog refreshDialog;

    public RefereshChannelsTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Runnable startProgress = new Runnable() {
            @Override
            public void run() {
                refreshDialog = ProgressDialog.show(context, null, "Initializing");
            }
        };
        TvChannelsActivity.getHandler().post(startProgress);

        StationsOpenHelper helper = new StationsOpenHelper(context);
        Channel[] channels;
        channels = helper.select();
        if (channels.length == 0) {
            ArrayList<Channel> channelsList = Parser.getChannels();
            channels = channelsList.toArray(new Channel[channelsList.size()]);
            helper.insert(channelsList);
        }
        TvChannelsActivity.setAdapter(channels);

        TvChannelsActivity.getHandler().removeCallbacks(startProgress);
        TvChannelsActivity.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (refreshDialog != null)
                    refreshDialog.dismiss();
            }
        });
    }
}
