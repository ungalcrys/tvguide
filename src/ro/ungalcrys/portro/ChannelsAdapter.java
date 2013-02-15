package ro.ungalcrys.portro;

import ro.ungalcrys.portro.task.LogoLoadingQueue;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelsAdapter extends BaseAdapter {
    // private static ImageView[] logoViews;
    // private static View[] views;
    private boolean[] markedViews;

    public ChannelsAdapter() {
        // logoViews = new ImageView[TvChannelsActivity.getChannels().length];
        // views = new View[TvChannelsActivity.getChannels().length];
        markedViews = new boolean[TvChannelsActivity.getChannels().length];
    }

    @Override
    public int getCount() {
        return TvChannelsActivity.getChannels().length;
    }

    @Override
    public Channel getItem(int index) {
        return TvChannelsActivity.getChannels()[index];
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if (views[position] == null) {
        View view = TvChannelsActivity.getInflater().inflate(R.layout.tv_stat_item, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView description = (TextView) view.findViewById(R.id.description);

        // Channel channel = TvChannelsActivity.getChannels()[position];
        // Util.printDebug("get bitmap logo for " + channel.getName());
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        // imageView.setImageBitmap(channel.getBitmap());
        // Util.scaleImage(imageView);
        // new LogoLoadingQueue(position, imageView);

        Bitmap localBitmap = TvChannelsActivity.getChannels()[position].getLocalBitmap();
        if (localBitmap == null)
            LogoLoadingQueue.add(position, imageView);
        else
            setLogo(position, imageView, localBitmap);

        name.setText(TvChannelsActivity.getChannels()[position].getName());
        description.setText("abcdefghgklsjdlksa");

        // views[position] = view;
        // }
        // return views[position];
        return view;
    }

    public void setLogo(int index, ImageView view, Bitmap bm) {
        view.setImageBitmap(bm);
        Util.scaleImage(view);
    }

    public boolean isMarked(int index) {
        return markedViews[index];
    }

    // public static void refreshLogo(final int index) {
    // Channel channel = TvChannelsActivity.getChannels()[index];
    // Util.printDebug("refresh logo for " + channel.getName());
    // final Bitmap bm = channel.getBitmap();
    //
    // if (views[index] == null)
    // return;
    //
    // final ImageView imageView = (ImageView) views[index].findViewById(R.id.image);
    // TvChannelsActivity.runOnUi(new Runnable() {
    // @Override
    // public void run() {
    // imageView.setImageBitmap(bm);
    // Util.scaleImage(imageView);
    // }
    // });
    // }

}
