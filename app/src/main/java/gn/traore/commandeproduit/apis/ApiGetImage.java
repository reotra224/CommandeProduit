package gn.traore.commandeproduit.apis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class ApiGetImage extends AsyncTask {

    private Context context;
    private ImageView imageView;
    private Bitmap bitmap = null;

    public ApiGetImage(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        //On forme l'url de l'image
        String linkImage = "http://www.tech.3s7.org/images/produits/" + String.valueOf(objects[0]);

        //On recupère l'image
        try {
            InputStream inputStream = new java.net.URL(linkImage).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Object o) {
        Bitmap bitmap = (Bitmap) o;
        Toast.makeText(context, String.valueOf(bitmap), Toast.LENGTH_LONG).show();
        imageView.setImageBitmap(bitmap);
    }
}
