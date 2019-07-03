package gn.traore.commandeproduit.apis;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import gn.traore.commandeproduit.AccueilFragment;
import gn.traore.commandeproduit.R;
import gn.traore.commandeproduit.model.Client;

import static gn.traore.commandeproduit.MainActivity.fragmentManager;

public class ApiInscription extends AsyncTask {
    private Context context;
    private int typeOperation;
    private Client client;

    public ApiInscription(Context context, int typeOperation) {
        this.context = context;
        this.typeOperation = typeOperation;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        client = new Client();
        try{
            client.setNom(String.valueOf(objects[0]));
            client.setPrenom(String.valueOf(objects[1]));
            client.setEmail(String.valueOf(objects[2]));
            client.setPhone(String.valueOf(objects[3]));
            client.setAdresse(String.valueOf(objects[4]));
            client.setAgent(String.valueOf(objects[5]));

            //String link="http://localhost/api/commande/inscription.php";
            String link = "http://tech.3s7.org/apiclient.php";
            String data  = URLEncoder.encode("nom", "UTF-8") + "=" +
                    URLEncoder.encode(client.getNom(), "UTF-8");
            data += "&" + URLEncoder.encode("prenom", "UTF-8") + "=" +
                    URLEncoder.encode(client.getPrenom(), "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(client.getEmail(), "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(client.getPhone(), "UTF-8");
            data += "&" + URLEncoder.encode("adresse", "UTF-8") + "=" +
                    URLEncoder.encode(client.getAdresse(), "UTF-8");
            data += "&" + URLEncoder.encode("agent", "UTF-8") + "=" +
                    URLEncoder.encode(client.getAgent(), "UTF-8");
            //On choisit le type d'opération à éffectuer
            if (typeOperation == 0) { // 0 = ADD
                data += "&" + URLEncoder.encode("param", "UTF-8") + "=" +
                        URLEncoder.encode("add", "UTF-8");
            } else { // 1 = EDITION
                data += "&" + URLEncoder.encode("param", "UTF-8") + "=" +
                        URLEncoder.encode("edition", "UTF-8");
            }

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch(Exception e){
            return "Exception1: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        String result = String.valueOf(o);
        //Si on reçoit le token
        if (!result.isEmpty()) {
            //On le concatène avec le phone
            String identifiants = client.getNom() + ";" +
                    client.getPrenom() + ";" +
                    client.getEmail() + ";" +
                    client.getPhone() + ";" +
                    client.getAdresse() + ";" +
                    client.getAgent() + ";" +
                    result;

            //Et on le stocke dans le fichier "identifiants"
            try {
                OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("identifiants", Context.MODE_PRIVATE));
                writer.write(identifiants);
                writer.close();

                //On crée le tableau des identifiants à passer au fragment Accueil.
                ArrayList<String> phoneToken = new ArrayList<>();
                phoneToken.add(client.getPhone());
                phoneToken.add(result);

                if (typeOperation == 0)
                    Toast.makeText(context, "Client inscrit avec succèss !", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, "Informations modifiées avec succèss !", Toast.LENGTH_LONG).show();

                //On lance le fragment Accueil
                AccueilFragment accueilFragment = AccueilFragment.getInstance(phoneToken);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, accueilFragment)
                        .commit();

            } catch (FileNotFoundException e) {
                Toast.makeText(context, "FileNotFoundException", Toast.LENGTH_LONG);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Sinon on affiche un message d'erreur
            Toast.makeText(context, "Token vide !", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
