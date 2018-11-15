package vs.xmlparse;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DescriptionChild extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView closeFragment, titleFragment, descriptionFragment;

    int position;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_child, container, false);
        closeFragment = view.findViewById(R.id.close);
        titleFragment = view.findViewById(R.id.fragment_title);
        descriptionFragment = view.findViewById(R.id.fragment_description);

        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                Toast toast = Toast.makeText(getActivity(),"Closed fragment", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        MainActivity mainactivity = (MainActivity) getActivity();

        String[] myTitles = mainactivity.getMyTitles();
        String[] myDescriptions = mainactivity.getMyDescriptions();
        int position = mainactivity.position();

        titleFragment.setText(myTitles[position]);
        descriptionFragment.setText(myDescriptions[position]);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }


}
