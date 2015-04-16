package yliu.andriod_player;


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yliu on 15-4-16.
 */
public class IptvXmlParser {

    private static final String ACTIVITY_TAG="IptvXmlParser";

    public List<NetEntry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            parser.setInput(in, null);
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<NetEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<NetEntry> entries = new ArrayList<NetEntry>();
        // Log.e(StackOverflowXmlParser.ACTIVITY_TAG, "" +  parser.getName());
        parser.require(XmlPullParser.START_TAG, null, "response");
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
            if (name.equals("attributes"))
                break;
        }

        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());
        parser.require(XmlPullParser.START_TAG, null, "attributes");
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
            if (name.equals("version")) {
                Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
                break;
            }
        }
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
            if (name.equals("liveType")) {
                Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
                break;
            }
        }
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());

        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());


        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        // parser.require(XmlPullParser.START_TAG, ns, "channel");
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());

        Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  parser.getName());
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +  name);
            // Starts by looking for the entry tag
            if (name.equals("liveType")) {

                ///

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    Log.e(IptvXmlParser.ACTIVITY_TAG, "" +name);
                    if (name.equals("channel")) {
                        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        entries.add(readEntry(parser));
                        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    } else {
                        skip(parser);
                    }
                    name = parser.getName();
                    Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    Log.e(IptvXmlParser.ACTIVITY_TAG, "" +name);
                }


                ///

            } else {
                skip(parser);
            }
        }
        return entries;

        //return null;
    }


    public static class NetEntry {
        public final String title;
        public final String link;

        private NetEntry(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }




    private NetEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "channel");
        String title = parser.getAttributeValue(null, "name");;
        String summary = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +name);
            // title =  parser.getAttributeValue(null, "id");
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" +title);
            summary = parser.getAttributeValue(null, "name");
            if (name.equals("text")) {
                //title = readTitle(parser);
            } else if (name.equals("summary")) {
                //  summary = readSummary(parser);
            } else if (name.equals("addressInfo")) {
                link = readLink(parser);
                Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            } else {
                skip(parser);
            }
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }

        parser.require(XmlPullParser.END_TAG, null, "channel");
        // Log.e(StackOverflowXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        return new NetEntry(title,link);
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "werrew";
        link = parser.getAttributeValue(null, "url");

        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + link);
        parser.nextTag();
        return link;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            throw new IllegalStateException();
        }
        int depth = 1;
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + XmlPullParser.END_TAG);
        Log.e(IptvXmlParser.ACTIVITY_TAG, "" + XmlPullParser.START_TAG);
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }

    }


}
