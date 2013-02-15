package ro.ungalcrys.portro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Util {
    public static final int ONE_DECISEC = 100;

    private static long start;

    public static void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            Log.e("", e.getMessage(), e);
        }
    }

    public static void saveUrlToFile(URL url, File file) throws IOException {
        Util.printDebug("save url to file");
        Util.printDebug(url.toString());
        Util.printDebug(file.toString());
        InputStream input = url.openStream();
        try {
            // The sdcard directory e.g. '/sdcard' can be used directly, or
            // more safely abstracted with getExternalStorageDirectory()
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }
    }

    public static void scaleImage(ImageView view) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(50);
        // Log.i("Test", "original width = " + Integer.toString(width));
        // Log.i("Test", "original height = " + Integer.toString(height));
        // Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        // Log.i("Test", "xScale = " + Float.toString(xScale));
        // Log.i("Test", "yScale = " + Float.toString(yScale));
        // Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        // Log.i("Test", "scaled width = " + Integer.toString(width));
        // Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        // Log.i("Test", "done");
    }

    private static int dpToPx(int dp) {
        float density = TvChannelsActivity.getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void startCounter() {
        start = System.currentTimeMillis();
    }

    public static void stopCounter(String message) {
        long duration = System.currentTimeMillis() - start;
        Log.d(TvChannelsActivity.TAG, message + duration);
    }

    public static void printException(Exception e) {
        Log.e(TvChannelsActivity.TAG, e.getMessage(), e);
    }

    public static void printDebug(String message) {
        Log.d(TvChannelsActivity.TAG, message);
    }

}
