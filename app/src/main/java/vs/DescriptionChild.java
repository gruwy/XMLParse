package vs.xmlparse;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DescriptionChild extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.description_child, container, false);

        TextView closeFragment = view.findViewById(R.id.close);

        TextView titleFragment = view.findViewById(R.id.fragment_title);

        TextView descriptionFragment = view.findViewById(R.id.fragment_description);

        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

                Toast toast = Toast.makeText(getActivity(),"Closed fragments by 'close' button ", Toast.LENGTH_SHORT);

                toast.show();
            }
        });


        MainActivity mainactivity = (MainActivity) getActivity();

        assert mainactivity != null;

        String[] myTitles = mainactivity.getMyTitles();

        String[] myDescriptions = mainactivity.getMyDescriptions();

        int position = mainactivity.position();

        titleFragment.setText(myTitles[position]);

        descriptionFragment.setText(myDescriptions[position]);

        return view;

    }

}
