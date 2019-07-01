package gn.traore.commandeproduit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class AccueilFragment extends Fragment {

    private ArrayList<String> identifiants;

    public AccueilFragment() {
        // Required empty public constructor
    }

    public static AccueilFragment getInstance(ArrayList<String> identifiants) {
        AccueilFragment accueilFragment = new AccueilFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("IDENTIFIANTS", identifiants);
        accueilFragment.setArguments(args);
        return accueilFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //On récupère les identifiants
        Bundle args = getArguments();
        if (args != null && args.containsKey("IDENTIFIANTS")) {
            identifiants = args.getStringArrayList("IDENTIFIANTS");
            Toast.makeText(view.getContext(), "Phone= " + identifiants.get(0) + " Token= " + identifiants.get(1), Toast.LENGTH_LONG).show();
        }
    }
}
