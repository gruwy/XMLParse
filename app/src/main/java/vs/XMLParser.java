package vs.xmlparse;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    private static final String ns = null;

    public List<InsideXML> parse(InputStream in) throws IOException {

        // Creating a new instance of pull parser, setting input and processing to next tag.
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            parser.nextTag();
            return readParkingLot(parser);
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        finally {
            in.close();
        }
    }

    // Processing the "Parking lot" to see if we have "car" tags. If we don't see it, the parser skips the tag. Returns a list.
    private List<InsideXML> readParkingLot(XmlPullParser parser) throws XmlPullParserException, IOException {

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

        // Initializing the strings
        String title = null;
        String description = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "description":
                    description = readDescription(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new InsideXML(title, description);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");

        return title;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");

        return description;
    }

    // For the tags title and description, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    // A skip method taken from https://developer.android.com/training/basics/network-ops/xml to skip tags.
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