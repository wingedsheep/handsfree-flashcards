package nl.wingedsheep.handsfreeflashcards.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wingedsheep.handsfreeflashcards.R;
import nl.wingedsheep.handsfreeflashcards.example.FrenchTestDeck;
import nl.wingedsheep.handsfreeflashcards.manager.PracticeRound;

public class PracticeFragment extends Fragment {
    private PracticeRound practiceRound = null;

    public PracticeFragment() {
        // Required empty public constructor
    }

    public static PracticeFragment newInstance() {
        PracticeFragment fragment = new PracticeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        practiceRound = new PracticeRound(new FrenchTestDeck(), getActivity());
        practiceRound.playMinutes(15);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice, container, false);
    }

    @Override
    public void onDestroyView() {
        if (practiceRound != null) {
            practiceRound.stop();
        }
        super.onDestroyView();
    }
}
