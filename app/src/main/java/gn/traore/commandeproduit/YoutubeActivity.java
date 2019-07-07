package gn.traore.commandeproduit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class YoutubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;

        String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"950\" height=\"1500\" src=\"https://www.youtube.com/channel/UCHbVKH_bkCNXCodTYbaWVNQ/about\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        //String utl = "https://www.youtube.com/channel/UCHbVKH_bkCNXCodTYbaWVNQ/about";
        WebView myWebView = findViewById(R.id.mWebView);
        myWebView.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT));

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadData(frameVideo, "text/html", "utf-8");
    }
}
