package vs.xmlparse;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    List<InsideXML> entries = null;

    int listViewPosition;

    ArrayList<String> arrayWithTitles = new ArrayList<>();
    ArrayList<String> arrayWithDescriptions = new ArrayList<>();

    private static final String URL = "http://192.168.31.254:8080/app.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating new instance of DownloadXML class
        DownloadXML downloadXML = new DownloadXML();

        // Executing ASyncTask to download, parse and display the data in list view
        downloadXML.execute(URL);

        // Initializing list view
        ListView listView = findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Assigning value to the int for further processing
                listViewPosition = position;

                //Fragment transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.add(R.id.parent_fragment_container, new DescriptionParent());

                ft.addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onBackPressed() {

        // This module checks if the back is pressed in the fragment and displays toast after closing the fragment stack
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

    // Position of an item in list view that is used for assigning an index in array for child fragment display
    public int position(){

        return listViewPosition;

    }

    // Methods to transfer values to display in child fragment
    public String[] getMyTitles() {

        return arrayWithTitles.toArray(new String[arrayWithTitles.size()]);

    }

    public String[] getMyDescriptions() {

        return arrayWithDescriptions.toArray(new String[arrayWithDescriptions.size()]);

    }

    // This class is an implementation of AsyncTask used for downloading XML feed from assigned URL.
    @SuppressLint("StaticFieldLeak")
    public class DownloadXML extends AsyncTask<String, Void, List<InsideXML>> {

        @Override
        protected void onPostExecute(List<InsideXML> result) {

            ListView listView = findViewById(R.id.listView);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayWithTitles);

            listView.setAdapter(adapter);

        }

        @Override
        protected List<InsideXML> doInBackground(String... urls) {

            // Trying to load xml from network using the first link provided by input stream
            try {
                return loadXmlFromNetwork(urls[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        private List<InsideXML> loadXmlFromNetwork(String urlString) {

            List<InsideXML> entries;

            // Calling InputStream and entry list
            InputStream stream;

            // Creating parser instance
            XMLParser xmlParser = new XMLParser();

            // Trying assigning a method to input stream and attempting to parse the information from provided input stream
            try {
                stream = downloadUrl(urlString);
                entries = xmlParser.parse(stream);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            //This cycle processes the list inside XML file and adds titles to arrayWithTitles and descriptions to arrayWithDescriptions for further display and more comfortable work with data
            for (InsideXML insideXML : entries)

            {
                arrayWithTitles.add(insideXML.title);
                arrayWithDescriptions.add(insideXML.description);
            }

            // Returns parsed entry list
            return entries;

        }

        private InputStream downloadUrl(String urlString) throws IOException {

            URL url = new URL(urlString);

            // Obtaining new HTTPURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Setting timeouts
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            // Setting a default method to get the *.xml file
            connection.setRequestMethod("GET");

            // Setting the URL connection for using it as input
            connection.setDoInput(true);

            // Starting the query
            connection.connect();

            // Getting an input stream from the connection operation
            return connection.getInputStream();

        }

    }

}