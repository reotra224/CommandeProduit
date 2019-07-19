package gn.traore.commandeproduit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YoutubeActivity extends AppCompatActivity {

    private List<String> idVideos = new ArrayList<>();
    private int currentVideo, precedent, next;
    private WebView myWebView;
    private String frameVideo;
    private  WebSettings webSettings;
    private Button btnNext, btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        //int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrecedent);

        myWebView = findViewById(R.id.mWebView);

        //On récupère les vidéos reçu
        String idsJson = (getIntent().hasExtra("LISTE_VIDEOS")) ?
                getIntent().getStringExtra("LISTE_VIDEOS") : "";
        //On désérialize la liste des vidéos
        JSONObject jsonObject;
        try {
            JSONArray jsonArray = new JSONArray(idsJson);
            for (int i=0; i<jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.idVideos.add(jsonObject.getString("lien"));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (this.idVideos.size() > 0) {
            lancerVideo(this.idVideos.indexOf(this.idVideos.get(0)));
        }
        //On écoutes les boutons Next et Preview
        ajouterEventSurBouton();
    }

    private void ajouterEventSurBouton() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idVideos.size() > 0) {
                    int videoSuivante = currentVideo + 1;
                    if (videoSuivante < idVideos.size() && videoSuivante >= 0) {
                        lancerVideo(videoSuivante);
                    } else {
                        Toast.makeText(YoutubeActivity.this, "Il n'y a plus d'autres vidéos !",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idVideos.size() > 0) {
                    int videoPrecedente = currentVideo - 1;
                    if (videoPrecedente < idVideos.size() && videoPrecedente >= 0) {
                        lancerVideo(videoPrecedente);
                    } else {
                        Toast.makeText(YoutubeActivity.this, "Il n'y a plus d'autres vidéos !",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void lancerVideo(int idVideo) {
        //On recupère la video en cour
        this.currentVideo = idVideo;
        //On forme le lien du Frame
        String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"950\" height=\"1500\" " +
                "src=\"https://www.youtube.com/embed/" + this.idVideos.get(idVideo) + "\" frameborder=\"0\" allowfullscreen>" +
                "</iframe></body></html>";

        webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadData(frameVideo, "text/html", "utf-8");
    }
}
