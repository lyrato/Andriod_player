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
public class IptvXmlParser
{

    private static final String ACTIVITY_TAG = "IptvXmlParser";

    public List<NetEntry> parse(InputStream in) throws XmlPullParserException, IOException
    {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<NetEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List<NetEntry> entries = new ArrayList<NetEntry>();
        parser.require(XmlPullParser.START_TAG, null, "response");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
           
            if (name.equals("attributes")) {
                break;
            }
        }

        parser.require(XmlPullParser.START_TAG, null, "attributes");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("version")) {
          
                break;
            }
        }

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("liveType")) {
                break;
            }
        }

     

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            // Starts by looking for the entry tag
            if (name.equals("liveType")) {
                ///
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    name = parser.getName();

                    if (name.equals("channel")) {
                        entries.add(readEntry(parser));
                    } else {
                        skip(parser);
                    }

                    name = parser.getName();
                }

                ///
            } else {
                skip(parser);
            }
        }

        return entries;
        //return null;
    }


    public static class NetEntry
    {
        public final String title;
        public final String link;

        private NetEntry(String title, String link)
        {
            this.title = title;
            this.link = link;
        }
    }




    private NetEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "channel");
        String title = parser.getAttributeValue(null, "name");;
        String summary = null;
        String link = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            // title =  parser.getAttributeValue(null, "id");
            summary = parser.getAttributeValue(null, "name");

            if (name.equals("text")) {
                //title = readTitle(parser);
            } else if (name.equals("summary")) {
                //  summary = readSummary(parser);
            } else if (name.equals("addressInfo")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }

        parser.require(XmlPullParser.END_TAG, null, "channel");
        // Log.e(StackOverflowXmlParser.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        return new NetEntry(title, link);
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String link = "werrew";
        link = parser.getAttributeValue(null, "url");
        parser.nextTag();
        return link;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;

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
