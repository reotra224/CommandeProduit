package gn.traore.commandeproduit.apis;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import gn.traore.commandeproduit.GestionProduit;
import gn.traore.commandeproduit.model.Produit;

public class ApiCommande extends AsyncTask {

    private Context context;

    public ApiCommande(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        StringBuilder sb;
        try {
            String token = String.valueOf(objects[0]);
            String link = "http://tech.3s7.org/apiproduit.php";

            String data  = URLEncoder.encode("tocken", "UTF-8") + "=" +
                    URLEncoder.encode(token, "UTF-8");

            URL url = new URL(link);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            wr.write( data );
            wr.flush();

            //Reception des données de la requête
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));

            sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            wr.close();
            reader.close();

            return sb.toString();

        } catch(Exception e) {
            return "Exception1: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        String result = String.valueOf(o);
        //On crée un Intent
        Intent intent = new Intent(context, GestionProduit.class);
        intent.putExtra("LISTE_PRODUITS", result);
        context.startActivity(intent);
    }
}
