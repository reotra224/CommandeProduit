package gn.traore.commandeproduit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import gn.traore.commandeproduit.apis.ApiInscription;
import gn.traore.commandeproduit.model.Client;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText nom, prenom, email, phone, address, agent;
    private Button btnInscrire;
    private Client client = null;
    private int typeOperation = 0;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment getInstance(String client) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("CLIENT", client);
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //On récupère les items du fragment
        recupItemsFragment(view);

        //On écoute le bouton
        btnInscrire.setOnClickListener(this);

        //On récupère le client reçu en paramètre
        Bundle args = getArguments();
        if (args != null && args.containsKey("CLIENT")) {
            String clientJson = args.getString("CLIENT");
            // On le désérialize en Client
            Gson gson = new Gson();
            Type type = new TypeToken<Client>() {}.getType();
            client = gson.fromJson(clientJson, type);
            //Toast.makeText(view.getContext(), client.getNom(), Toast.LENGTH_LONG).show();
            //On rempli le formulaire avec les infos du client
            nom.setText(client.getNom());
            prenom.setText(client.getPrenom());
            email.setText(client.getEmail());
            phone.setText(client.getPhone());
            address.setText(client.getAdresse());
            agent.setText(client.getAgent());
            //On change le label du bouton
            btnInscrire.setText("MODIFIER");
            //On met le type d'opération à EDITION=1
            typeOperation = 1;
        } else {
            //On met le type d'opération à ADD=1
            typeOperation = 0;
        }
    }

    private void recupItemsFragment(View view) {
        nom = view.findViewById(R.id.editNom);
        prenom = view.findViewById(R.id.editPrenom);
        email = view.findViewById(R.id.editEmail);
        phone = view.findViewById(R.id.editPhone);
        address = view.findViewById(R.id.editAdresse);
        agent = view.findViewById(R.id.editAgent);
        btnInscrire = view.findViewById(R.id.btnInscrire);
    }

    @Override
    public void onClick(View view) {
        //TODO Vérifier que le phone/certains champs ne sont pas vide
        //On envoie les données à l'API d'inscription
        new ApiInscription(view.getContext(), typeOperation).execute(
                nom.getText(),
                prenom.getText(),
                email.getText(),
                phone.getText(),
                address.getText(),
                agent.getText());
    }
}
