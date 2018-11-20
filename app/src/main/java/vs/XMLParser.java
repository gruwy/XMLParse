package vs.xmlparse;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    private static final String ns = null;

    public List<InsideXML> parse(InputStream in) throws XmlPullParserException, IOException {
    try{
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        parser.next();
        parser.nextTag();
        return readParkingLot(parser);}
        finally {
        in.close();
        }
    }

    public List<InsideXML> readParkingLot(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<InsideXML> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "parkinglot");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("car")) {
                entries.add(readCar(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private InsideXML readCar(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "car");
        String title = null;
        String description = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else {
                skip(parser);
            }
        }

        return new InsideXML(title, description);
    }

    // Processes title tags in the feed.
    public String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");

        return title;
    }

    // Processes summary tags in the feed.
    public String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");

        return description;
    }

    // For the tags title and summary, extracts their text values.
    public String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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