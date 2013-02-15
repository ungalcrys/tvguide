package ro.ungalcrys.portro.task;

import java.util.LinkedList;

import ro.ungalcrys.portro.TvChannelsActivity;
import ro.ungalcrys.portro.Util;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class LogoLoadingQueue {
    private static final LinkedList<Pair> QUEUE = new LinkedList<Pair>();

    private static boolean isProcessing = false;

    public static void add(int index, ImageView view) {
        QUEUE.addFirst(new Pair(index, view));
        processQueue();
    }

    public static void processQueue() {
        if (isProcessing)
            return;

        new Thread() {
            @Override
            public void run() {
                isProcessing = true;
                while (QUEUE.size() > 0) {
                    final Pair pair = QUEUE.pollFirst();
                    if (TvChannelsActivity.getAdapter().isMarked(pair.index))
                        continue;

                    final Bitmap bitmap = TvChannelsActivity.getChannels()[pair.index].getBitmap();
                    TvChannelsActivity.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            TvChannelsActivity.getAdapter().setLogo(pair.index, pair.view, bitmap);
                        }
                    });
                    Util.sleep(100);
                }
                isProcessing = false;
            }
        }.start();
    }

    private static class Pair {
        private final int index;
        private final ImageView view;

        public Pair(int index, ImageView view) {
            this.index = index;
            this.view = view;
        }
    }
}
