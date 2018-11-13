package vs.xmlparse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://127.0.0.1:8080";
    private List<InsideXML> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        XMLParser xmlParser = new XMLParser(this);
        List<InsideXML> entries = new ArrayList<>();

        try {
            xmlParser.parse();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }


        ArrayAdapter<InsideXML> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(adapter);


    }}