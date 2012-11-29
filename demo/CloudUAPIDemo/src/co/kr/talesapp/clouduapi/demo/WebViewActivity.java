package co.kr.talesapp.clouduapi.demo;

import kr.co.talesapp.clouduapi.CloudUFS;
import kr.co.talesapp.clouduapi.OAuthTokens;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends Activity {

    @TargetApi(7)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		setContentView(webView);
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent(); 
        String url="";
        if (extras != null) {
            url = extras.getString("URL");
            if(url!=null) {
                webView.loadUrl(url);
            	Log.d("onCreate", url);
            }
        }
        if(Intent.ACTION_VIEW.equals(intent.getAction())) { 
            Uri uri = intent.getData();
            
            // box
            String ticket = uri.getQueryParameter("ticket"); 
            String auth_token = uri.getQueryParameter("auth_token");
            if(ticket!=null && auth_token!=null) {
            	CUSetting setting=new CUSetting(this);
            	setting.boxToken=auth_token;
				setting.saveSetting();
				Toast.makeText(this, "Saved Box OAuth Setting", Toast.LENGTH_SHORT).show();
            }
            // dropbox
            String uid = uri.getQueryParameter("uid"); 
            String oauth_token = uri.getQueryParameter("oauth_token");
            if(uid!=null && oauth_token!=null) {
            	CUSetting setting=new CUSetting(this);
            	OAuthTokens oAuthTokens=new OAuthTokens();
            	oAuthTokens.setOAuthToken(setting.dBoxToken);
            	oAuthTokens.setOAuthTokenSecret(setting.dBoxTokenSecret);
                oAuthTokens.setConsumerKey(setting.dBoxApiKey);
                oAuthTokens.setConsumerSecret(setting.dBoxApiSecret);
                CloudUFS cloudUFS=new CloudUFS();
                cloudUFS.getAccessToken(ICloudFS.CloudType.DROPBOX, oAuthTokens);
                setting.dBoxToken=oAuthTokens.getOAuthToken();
                setting.dBoxTokenSecret=oAuthTokens.getOAuthTokenSecret();
                setting.saveSetting();
				Toast.makeText(this, "Saved Drop Box OAuth Setting", Toast.LENGTH_SHORT).show();
            }
            
            Log.i("onCreate", "uri : "+uri.toString());
            finish();
        } 
    }
}
