package vs.xmlparse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public ArrayList<String> arrayWithTitles = new ArrayList<String>();

    public ArrayList<String> arrayWithDescription = new ArrayList<String>();

    public static final String WIFI = "Wi-Fi";

    private static final String URL = "http://127.0.0.1:8080/app.xml";

    private static boolean wifiConnected = false;

    public static String sPref = null;

    List<InsideXML> entries = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPage();

        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayWithTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void loadPage() {
        if ((sPref.equals(WIFI)) && (wifiConnected)) {
            new DownloadXmlTask().execute(URL);
        } else {
        }
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.app_name);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.app_name);
            }
        }

        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            XMLParser xmlParser = new XMLParser();
            List<InsideXML> entries = null;
            StringBuilder htmlString = new StringBuilder();

            try {
                stream = downloadUrl(urlString);
                entries = xmlParser.parse(stream);
            }
            catch (Exception ex) {}

            finally {
                if (stream != null) {
                    stream.close(); }
            }

            for (InsideXML insideXML : entries) {
                arrayWithTitles.add(insideXML.title);
                arrayWithDescription.add(insideXML.description);
            }

            return htmlString.toString();
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            return conn.getInputStream();
        }
    }

}