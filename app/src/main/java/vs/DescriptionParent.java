package vs.xmlparse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Objects;

public class DescriptionParent extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.description_parent, container, false);

        FrameLayout layout = view.findViewById(R.id.child_fragment_container);

        layout.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Fragment childFragment = new DescriptionChild();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(getActivity(), "Closed fragments from clicking on semi-transparent area", Toast.LENGTH_SHORT).show();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

}
