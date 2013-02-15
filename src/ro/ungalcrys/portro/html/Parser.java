package ro.ungalcrys.portro.html;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ro.ungalcrys.portro.Channel;
import ro.ungalcrys.portro.Util;

public class Parser {

    public static final ArrayList<Channel> getChannels() {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        Document doc = getDocument("http://port.ro/pls/tv/tv.prog");
        if (doc != null) {
            Elements channelCombo = doc.select("select#channelCombo");
            Element element = channelCombo.get(0);
            for (Element channelCategory : element.children()) {
                for (Element channelElement : channelCategory.children()) {
                    int id = Integer.valueOf(channelElement.attr("value"));
                    String name = channelElement.ownText();
                    Channel tvChannel = new Channel(id, name);
                    channels.add(tvChannel);
                }
            }
        }
        return channels;
    }

    public static String getLogo(Channel channel) {
        Document doc = getDocument(channel.getUrl());
        if (doc != null) {
            Elements images = doc.getElementsByTag("img");
            Util.printDebug("img elemnts count " + images.size());
            for (Element img : images) {
                if (img.attr("alt").equals(channel.getName())) {
                    return img.attr("src");
                }
            }
        }
        return null;
    }

    private static Document getDocument(final String url) {
        return new ConnectCommand<Document>() {
            @Override
            protected Document doConnectAction() throws IOException {
                Document document = Jsoup.connect(url).get();
                if (document == null)
                    Util.printDebug("null document for url " + url);
                return document;
            }
        }.getResult();
    }
}
