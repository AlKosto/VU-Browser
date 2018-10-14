package com.example.imran.vubrowser;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar VUPrograssBar;
    private  ImageView VUImageView;
    private WebView VuWebView;
    private LinearLayout VULinearLayout;
    private String myCurrentUrl;

    private SwipeRefreshLayout VUSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        VUPrograssBar =(ProgressBar) findViewById(R.id.myProgressBar);
        VuWebView = (WebView) findViewById(R.id.myWebView);
        VUImageView =(ImageView) findViewById(R.id.myImageView);
        VULinearLayout =(LinearLayout) findViewById(R.id.myLinearLayout) ;
        VUSwipeLayout =(SwipeRefreshLayout) findViewById(R.id.mySwipeLayout);

        VUPrograssBar.setMax(100);

        VuWebView.loadUrl("https://www.google.com/");
        VuWebView.getSettings().setJavaScriptEnabled(true);

        VuWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                VULinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                VUSwipeLayout.setRefreshing(false);
                VULinearLayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                myCurrentUrl = url;
            }
        });

        VuWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                VUPrograssBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                VUImageView.setImageBitmap(icon);
            }



        });


        VuWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request vuReuest = new DownloadManager.Request(Uri.parse(url));
                vuReuest.allowScanningByMediaScanner();
                vuReuest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager vuManager =(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                vuManager.enqueue(vuReuest);

                Toast.makeText(MainActivity.this, "Your File Is Downloading", Toast.LENGTH_SHORT).show();
            }
        });

        VUSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VuWebView.reload();
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (VuWebView.canGoBack()){
            VuWebView.goBack();
        }
        else
        {
            finish();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vu_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_forward:

                onFrowardPressed();
                break;

            case R.id.menu_refresh:
                VuWebView.reload();
                break;

            case R.id.menu_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, myCurrentUrl);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Copied URL");
                startActivity(Intent.createChooser(shareIntent, "Share URL"));
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void onFrowardPressed() {
        if(VuWebView.canGoForward()){
            VuWebView.goForward();
        }else{
            Toast.makeText(this, "There is no forward page", Toast.LENGTH_SHORT).show();
        }
    }
}
