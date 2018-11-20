package vs.xmlparse;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DescriptionParent.OnFragmentInteractionListener, DescriptionChild.OnFragmentInteractionListener {

    XMLParser xmlParser = new XMLParser();

    List<InsideXML> entries = null;

    int a;

    private static final String TAG = "XMLParse";
    private static final String URL = "http://192.168.31.254:8080/app.xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadXML task = new DownloadXML();
        task.execute(URL);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                a = position;

                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.parent_fragment_container, new DescriptionParent());
                ft.addToBackStack(null).commit();

            }
        });*/
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
            Toast toast = Toast.makeText(MainActivity.this,"Closed fragments by 'back' button ", Toast.LENGTH_SHORT);
            toast.show();
        }

        super.onBackPressed();
    }

    public int position(){

        return a;
    }


    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i(TAG, "received communication from parent fragment");
    }

    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i(TAG, "received communication from child fragment");
    }

    public class DownloadXML extends AsyncTask<String, Void, List<InsideXML>> {

        ArrayList<String> arrayWithTitles = new ArrayList<String>();
        ArrayList<String> arrayWithDescriptions = new ArrayList<String>();

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<InsideXML> result) {
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, arrayWithTitles);
        listView.setAdapter(adapter);
        }

        @Override
        protected List<InsideXML> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return entries;
            } catch (XmlPullParserException e) {
                return entries;
            }
        }

        private List<InsideXML> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            XMLParser xmlParser = new XMLParser();
            List<InsideXML> entries = null;

            try {
                Log.d(TAG, "Trying to download");
                stream = downloadUrl(urlString);
                Log.d(TAG, "Trying to parse");
                entries = xmlParser.parse(stream);
                Log.d(TAG, "Done");
            } finally {
                if (stream != null) {
                    Log.d(TAG, "Closing stream");
                    stream.close();
                }
            }

            for (InsideXML insideXML : entries)

            {
                arrayWithTitles.add(insideXML.title);
                arrayWithDescriptions.add(insideXML.description);
            }

            return entries;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }


        public String[] getMyTitles() {
            return arrayWithTitles.toArray(new String[arrayWithTitles.size()]);
        }
        public String[] getMyDescriptions() {
            return arrayWithDescriptions.toArray(new String[arrayWithDescriptions.size()]);
        }
    }


}