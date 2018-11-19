package vs.xmlparse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DescriptionParent.OnFragmentInteractionListener, DescriptionChild.OnFragmentInteractionListener {

    XMLParser xmlParser = new XMLParser(this);

    ArrayList<String> arrayWithTitles = new ArrayList<String>();
    ArrayList<String> arrayWithDescriptions = new ArrayList<String>();

    List<InsideXML> entries = null;

    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.listView);

        try {
            entries = xmlParser.parse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (InsideXML insideXML : entries)

        {
            arrayWithTitles.add(insideXML.title);
            arrayWithDescriptions.add(insideXML.description);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayWithTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                a = position;

                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.parent_fragment_container, new DescriptionParent());
                ft.addToBackStack(null).commit();

            }
        });

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

    public String[] getMyTitles() {
        return arrayWithTitles.toArray(new String[arrayWithTitles.size()]);
    }

    public String[] getMyDescriptions() {
        return arrayWithDescriptions.toArray(new String[arrayWithDescriptions.size()]);
    }

    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }
}