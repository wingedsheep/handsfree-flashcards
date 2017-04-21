package nl.wingedsheep.handsfreeflashcards.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import nl.wingedsheep.handsfreeflashcards.R;

public class AddDeckFragment extends Fragment {


    public AddDeckFragment() {
        // Required empty public constructor
    }

    public String getTitle() {
        return "Add deck";
    }

    public static AddDeckFragment newInstance() {
        AddDeckFragment fragment = new AddDeckFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_deck, container, false);

        AppCompatSpinner selectFirstLanguage = (AppCompatSpinner) view.findViewById(R.id.input_first_language);
        String[] languageOptions = new String[] {
                "Dutch", "French"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, languageOptions);
        selectFirstLanguage.setAdapter(adapter);

        AppCompatSpinner selectSecondLanguage = (AppCompatSpinner) view.findViewById(R.id.input_second_language);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, languageOptions);
        selectSecondLanguage.setAdapter(adapter2);
        selectSecondLanguage.setSelection(1);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Activity) context).setTitle(getTitle());
    }

}
