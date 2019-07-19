package gn.traore.commandeproduit.apis;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import gn.traore.commandeproduit.YoutubeActivity;

public class ApiVideo extends AsyncTask {

    private Context context;

    public ApiVideo(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            String token = String.valueOf(objects[0]);
            String link = "http://tech.3s7.org/apivideos.php";

            String data = null;
            data = URLEncoder.encode("tocken", "UTF-8") + "=" +
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
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            wr.close();
            reader.close();

            return sb.toString();

        } catch (Exception e) {
            return "EXCEPTION";
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        String result = String.valueOf(o);
        if (result.equalsIgnoreCase("ERROR")) {
            Toast.makeText(context, "Erreur dans l'identification du client !", Toast.LENGTH_SHORT).show();
        } else if (result.equalsIgnoreCase("EXCEPTION")) {
            Toast.makeText(context, "Erreur de connexion au serveur !", Toast.LENGTH_SHORT).show();
        } else if(result.isEmpty()) {
            Toast.makeText(context, "Aucune vidéo trouvée !", Toast.LENGTH_SHORT).show();
        } else {
            //On crée un Intent
            Intent intent = new Intent(context, YoutubeActivity.class);
            intent.putExtra("LISTE_VIDEOS", result);
            context.startActivity(intent);
        }
    }
}
