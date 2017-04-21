package nl.wingedsheep.handsfreeflashcards.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wingedsheep.handsfreeflashcards.R;

public class DecksFragment extends Fragment {

    private OnAddDeckButtonClickedListener mListener;

    public DecksFragment() {
        // Required empty public constructor
    }

    public String getTitle() {
        return "Decks";
    }

    public static DecksFragment newInstance() {
        DecksFragment fragment = new DecksFragment();
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
        View view =  inflater.inflate(R.layout.fragment_decks, container, false);

        FloatingActionButton addDeck = (FloatingActionButton) view.findViewById(R.id.fab);
        addDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddDeckButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Activity) context).setTitle(getTitle());
        if (context instanceof OnAddDeckButtonClickedListener) {
            mListener = (OnAddDeckButtonClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddDeckButtonClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddDeckButtonClickedListener {
        public void onAddDeckButtonClicked();
    }
}
