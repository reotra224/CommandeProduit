package gn.traore.commandeproduit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean inscriptionOK;
    private ArrayList<String> data = new ArrayList<>();
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        //deleteFile("identifiants");

        inscriptionOK = verifInscription();

        configureEtAfficherLesFragments(inscriptionOK, data);

    }

    /**
     * Permet de vérifier que l'utilisateur est déjà inscrit.
     * @return true ou false
     */
    private boolean verifInscription() {
        String result = "";
        try {
            //On ouvre le fichier identifiant
            InputStream inputStream = openFileInput("identifiants");

            //Si le fichier existe
            if (inputStream != null) {
                //On crée le lecteur du fichier
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                //Variable stockant les lignes du fichier
                String ligne;
                StringBuilder buf = new StringBuilder();
                //On lit le fichier
                while ((ligne = reader.readLine()) != null) {
                    buf.append(ligne + "\r\n");
                }
                //On ferme le flux
                reader.close();
                inputStream.close();
                //On récupère le contenu du fichier lu
                result = buf.toString();
                if (!result.isEmpty()) {
                    for (String str : result.split(";")) {
                        data.add(str);
                    }
                }
            }
        }catch (FileNotFoundException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result.isEmpty();
    }

    /**
     * Permet de gérer les fragments à afficher en fonction d'un boolean.
     * @param inscriptionOK
     * @param data
     */
    private void configureEtAfficherLesFragments(boolean inscriptionOK, ArrayList<String> data) {
        if (!inscriptionOK) {
            //On forme les identifiants à envoyer au fragement Accueil
            ArrayList<String> identifiants = new ArrayList<>();
            identifiants.add(data.get(3));
            identifiants.add(data.get(6));

            //Ensuite on lance le fragment Accueil
            AccueilFragment accueilFragment = AccueilFragment.getInstance(identifiants);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, accueilFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
        super.onBackPressed();
    }
}
