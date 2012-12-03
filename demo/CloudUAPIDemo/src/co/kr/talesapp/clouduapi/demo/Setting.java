package co.kr.talesapp.clouduapi.demo;

import java.net.URLEncoder;

import kr.co.talesapp.clouduapi.CloudUFS;
import kr.co.talesapp.clouduapi.OAuthTokens;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class Setting extends Activity implements OnClickListener {
	Button boxButton;
	Button dBoxButton;
	Button uOneButton;
	CloudUFS cloudUFS=new CloudUFS();
	CUSetting setting;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.setting);
    	
    	boxButton=(Button)findViewById(R.id.button1);
    	dBoxButton=(Button)findViewById(R.id.button2);
    	uOneButton=(Button)findViewById(R.id.button3);
    	boxButton.setOnClickListener(onClickListener);
    	dBoxButton.setOnClickListener(onClickListener);
    	uOneButton.setOnClickListener(onClickListener);
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			String url="";
			OAuthTokens oAuthTokens=new OAuthTokens();

			Log.d("CloudUAPIDemo", "onClick");
			switch(v.getId()){
			case R.id.button1:
				oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.BOX, "", null);
				Log.d("CloudUAPIDemo", oAuthTokens.getTicket());
				url=cloudUFS.prop.getProperty("BoxFS.authorize")+oAuthTokens.getTicket();
		    	setting=new CUSetting(getApplicationContext());
				setting.boxApiKey="";
				setting.saveSetting();
				createWebView(url);
				break;
			case R.id.button2:
				oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.DROPBOX, "", "");
				Log.d("CloudUAPIDemo", oAuthTokens.getOAuthToken()+", "+oAuthTokens.getOAuthTokenSecret());
				url=cloudUFS.prop.getProperty("DropboxFS.authorize")+"?oauth_token="+oAuthTokens.getOAuthToken()+
					"&oauth_consumer_key=&oauth_callback="+
					URLEncoder.encode("cudemo://co.kr.talesapp.clouduapi.demo");
		    	setting=new CUSetting(getApplicationContext());
				setting.dBoxApiKey="";
				setting.dBoxApiSecret="";
				setting.dBoxToken=oAuthTokens.getOAuthToken();
				setting.dBoxTokenSecret=oAuthTokens.getOAuthTokenSecret();
				setting.saveSetting();
				createWebView(url);
				break;
			case R.id.button3:
				oAuthTokens=cloudUFS.getRequestToken(ICloudFS.CloudType.UBUNTUONE, "", "");
				oAuthTokens=cloudUFS.getAccessToken(ICloudFS.CloudType.UBUNTUONE, oAuthTokens);
		    	setting=new CUSetting(getApplicationContext());
				setting.uOneConsumer=oAuthTokens.getConsumerKey();
				setting.uOneConsumerSecret=oAuthTokens.getConsumerSecret();
				setting.uOneToken=oAuthTokens.getOAuthToken();
				setting.uOneTokenSecret=oAuthTokens.getOAuthTokenSecret();
				setting.saveSetting();
				Log.d("CloudUAPIDemo", oAuthTokens.getOAuthToken()+", "+oAuthTokens.getOAuthTokenSecret()+", "+oAuthTokens.getConsumerKey()+", "+oAuthTokens.getConsumerSecret());
				Toast.makeText(getApplicationContext(), "Saved Ubuntu One OAuth Setting", Toast.LENGTH_SHORT).show();
				break;
          }
		}
    };
    
    public void createWebView(String url) {
		Intent webView = new Intent(this, WebViewActivity.class);
		webView.putExtra("URL", url);
		startActivity(webView);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
