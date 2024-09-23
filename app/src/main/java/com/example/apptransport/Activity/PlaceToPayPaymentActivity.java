package com.example.apptransport.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayGlobales;
import com.apreciasoft.mobile.asremis.R;

public class PlaceToPayPaymentActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_to_pay_payment);
        initializeControls();
        loadUrlToWebView();
    }

    private void initializeControls()
    {
        ImageButton imageButtonCerrar= findViewById(R.id.imageButton_cerrar);
        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = myWebView.getUrl();
                if("".equals(url))
                {
                    closeActivityWhenFinishPaymentProcess(true);
                }
                else{
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    private void loadUrlToWebView(){
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            try{
                String urlPrincipal = extras.getString(PlaceToPayGlobales.PARAM_URL_PAYMENT,"");

                myWebView =  findViewById(R.id.webview);
                myWebView.setWebChromeClient(new WebChromeClient());
                myWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url.startsWith(PlaceToPayGlobales.REDIRECT_URL_OK) || url.startsWith(PlaceToPayGlobales.REDIRECT_URL_CANCEL)) {
                            closeActivityWhenFinishPaymentProcess(url.startsWith(PlaceToPayGlobales.REDIRECT_URL_OK));
                            return true;
                        }
                        return false;
                    }
                });

                WebSettings webSettings = myWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAppCacheEnabled(false);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                myWebView.loadUrl(urlPrincipal);

                WebViewClient a = new WebViewClient();



            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.error_redirigir_a_pago, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.no_existe_url, Toast.LENGTH_LONG).show();
        }

    }

    private void closeActivityWhenFinishPaymentProcess(boolean isOk)
    {
        Intent data = new Intent();
        String textToReturn = isOk? "OK": "CANCEL";
        data.setData(Uri.parse(textToReturn));
        setResult(RESULT_OK, data);
        finish();
    }


}
