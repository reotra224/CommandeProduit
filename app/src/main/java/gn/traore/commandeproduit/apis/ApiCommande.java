package gn.traore.commandeproduit.apis;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import gn.traore.commandeproduit.GestionProduit;
import gn.traore.commandeproduit.Panier;
import gn.traore.commandeproduit.model.ProduitDTO;

public class ApiCommande extends AsyncTask {

    private Context context;
    private String phone;
    private String token;
    private int typeOperation;

    public ApiCommande(Context context, String phone, int typeOperation) {
        this.context = context;
        this.phone = phone;
        this.typeOperation = typeOperation;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        StringBuilder sb = new StringBuilder();

        if (typeOperation == 0) {
            try {
                token = String.valueOf(objects[0]);
                String link = "http://tech.3s7.org/apiproduit.php";

                String data = URLEncoder.encode("tocken", "UTF-8") + "=" +
                        URLEncoder.encode(token, "UTF-8");

                URL url = new URL(link);
                URLConnection connection = url.openConnection();

                connection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

                wr.write(data);
                wr.flush();

                //Reception des données de la requête
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                wr.close();
                reader.close();

                return sb.toString();

            } catch (Exception e) {
                return "Exception1: " + e.getMessage();
            }
        }
        else {
            try {
                //on récupère les arguments reçu
                token = String.valueOf(objects[1]);
                //On forme le lien de la requête vers l'API
                String link = "http://tech.3s7.org/apicommande.php";

                //On forme les données à envoyer
                String produitDtoJson = formaterLesDonnees(token, String.valueOf(objects[0]));

                //On ouvre la connexion avec l'API
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                //On ajoute les données au corps de la requête

                OutputStream os = connection.getOutputStream();
                byte[] input = produitDtoJson.getBytes("utf-8");
                os.write(input, 0, input.length);

                    //Reception de la reception de l'API
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                //On ferme les flux
                os.close();
                reader.close();

                return sb.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String formaterLesDonnees(String token, String produitDtoJson) {
        JSONObject data = new JSONObject();
        try {
            JSONArray jr = new JSONArray(produitDtoJson);
            data.put("tocken", token);
            data.put("produitDto", jr);

            return data.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        String result = String.valueOf(o);
        if (result.equalsIgnoreCase("ERROR")) {
            Toast.makeText(context, "Erreur dans l'identification du client !", Toast.LENGTH_SHORT).show();
        } else {
            if (typeOperation == 0) {
                //On crée un Intent
                Intent intent = new Intent(context, GestionProduit.class);
                intent.putExtra("LISTE_PRODUITS", result);
                intent.putExtra("PHONE", phone);
                intent.putExtra("TOKEN", token);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Commande enregistrée avec succès !",
                        Toast.LENGTH_SHORT).show();

                // On vide le panier
                Panier.nettoyerPanier(context);
            }
        }
    }
}
