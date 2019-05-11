package vmap.a2016.khkt.myvmap.AppActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import vmap.a2016.khkt.myvmap.HTML5WebView.HTML5WebView;

public class video_activity extends AppCompatActivity {
    HTML5WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_video);
       // WebView myVideo = (WebView) findViewById(R.id.webView);
       // myVideo.setWebChromeClient(new WebChromeClient());
       // myVideo.setWebViewClient(new WebViewClient() {
        //    @Override
        //    public boolean shouldOverrideUrlLoading(WebView view, String url) {
       //         return false;
        //    }
       // });
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("getVIDEO");
        String myText = packageFromCaller.getString("VIDEO");
      //  myVideo.getSettings().setJavaScriptEnabled(true);

        //myVideo.loadData(myText, "text/html", "utf-8");
       // myVideo.loadUrl(myText);;
        mWebView = new HTML5WebView(video_activity.this);

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.loadUrl(myText);
        }

        setContentView(mWebView.getLayout());
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
                mWebView.hideCustomView();
                //  mWebView.goBack();
                //mWebView.goBack();6
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
