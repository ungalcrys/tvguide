package ro.ungalcrys.portro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import ro.ungalcrys.portro.html.Parser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Channel {
    public static final String DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    // database bind columns
    private final String name;
    private final int id;

    // computed columns
    private String url;
    private Bitmap bitmap;

    public Channel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        if (url == null) {
            url = "http://port.ro/pls/w/tv.channel?i_ch=" + id + "&i_date=" + DATE + "&i_where=1";
            Util.printDebug("computed url: " + url);
        }
        return url;
    }

    public Bitmap getLocalBitmap() {
        File file = new File(TvChannelsActivity.getContext().getFilesDir(), "logos");
        File localFile = new File(file, String.valueOf(getId()));

        if (bitmap == null) {
            try {
                content = new FileInputStream(localFile);
                Util.printDebug("found local bitmap for " + getName());
            } catch (FileNotFoundException e) {
                Util.printDebug("local bitmap not found for " + getName());
            }
            bitmap = BitmapFactory.decodeStream(content);
        }
        return bitmap;
    }

    public Bitmap getBitmap() {
        File file = new File(TvChannelsActivity.getContext().getFilesDir(), "logos");

        // TODO use it only at application start
        file.mkdirs();

        File localFile = new File(file, String.valueOf(getId()));

        if (bitmap == null) {
            String logoPath = Parser.getLogo(this);
            URL logoUrl = null;
            try {
                logoUrl = new URL(logoPath);
            } catch (MalformedURLException e) {
                Util.printException(e);
            }
            try {
                Util.saveUrlToFile(logoUrl, localFile);
            } catch (IOException e) {
                Util.printException(e);
            }
            try {
                content = (InputStream) logoUrl.getContent();
            } catch (IOException e) {
                Util.printException(e);
            }
            bitmap = BitmapFactory.decodeStream(content);
            Util.printDebug("found remote bitmap for " + getName());
        }
        return bitmap;
    }

    InputStream content;

    public int getId() {
        return id;
    }
}
