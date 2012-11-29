package co.kr.talesapp.clouduapi.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class CUSetting {
	/*
		Log.d("CUSetting", "getSetting : "+this.boxApiKey);
		Log.d("CUSetting", "getSetting : "+this.boxToken);
		Log.d("CUSetting", "getSetting : "+this.uOneToken);
		Log.d("CUSetting", "getSetting : "+this.uOneTokenSecret);
		Log.d("CUSetting", "getSetting : "+this.uOneConsumer);
		Log.d("CUSetting", "getSetting : "+this.uOneConsumerSecret);
		Log.d("CUSetting", "getSetting : "+this.dBoxApiKey);
		Log.d("CUSetting", "getSetting : "+this.dBoxApiSecret);
		Log.d("CUSetting", "getSetting : "+this.dBoxToken);
		Log.d("CUSetting", "getSetting : "+this.dBoxTokenSecret);

	11-30 04:57:51.723: D/CUSetting(10618): getSetting : jahyd179weeszjd0rur0vucpqyl5e4fv
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : lq9rcsz70hzzifjshrlpirbo359va04m
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : pcStGXaaDkyOWzQBzmECeozxDHGEBaRHeaqaaVJoQamEXGSgHu
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : ATUWAgsEzrTlKcPlWzKolYhROBxNGdREchBQIfzGhwlrrJmRvj
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : EDp6FXB
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : iUyUXUQLzNqUnBYUoKUrPCSRXcYfWH
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : 717ci3if3xszc67
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : fzd7dldtkrzay3b
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : 87kfsrhi7ba9a6q
	11-30 04:57:51.723: D/CUSetting(10618): getSetting : 4yp9l2cqla25hi4
	*/

	public String boxApiKey;
	public String boxToken;
	
	public String uOneToken;
	public String uOneTokenSecret;
	public String uOneConsumer;
	public String uOneConsumerSecret;
	
	public String dBoxApiKey;
	public String dBoxApiSecret;
	public String dBoxToken;
	public String dBoxTokenSecret;
	public static boolean init=false;
	SharedPreferences settings;
	
	public CUSetting(Context context){
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		 getSettings();
	}
	
	public void saveSetting() {
		final Editor et=settings.edit();
		et.putString("boxApiKey", this.boxApiKey);
		et.putString("boxToken", this.boxToken);
		
		et.putString("uOneToken", this.uOneToken);
		et.putString("uOneTokenSecret", this.uOneTokenSecret);
		et.putString("uOneConsumer", this.uOneConsumer);
		et.putString("uOneConsumerSecret", this.uOneConsumerSecret);

		et.putString("dBoxApiKey", this.dBoxApiKey);
		et.putString("dBoxApiSecret", this.dBoxApiSecret);
		et.putString("dBoxToken", this.dBoxToken);
		et.putString("dBoxTokenSecret", this.dBoxTokenSecret);

		et.commit();
	}

	public void getSettings() {
		init=true;

		this.boxApiKey=settings.getString("boxApiKey", "");
		this.boxToken=settings.getString("boxToken", "");
		
		this.uOneToken=settings.getString("uOneToken", "");
		this.uOneTokenSecret=settings.getString("uOneTokenSecret", "");
		this.uOneConsumer=settings.getString("uOneConsumer", "");
		this.uOneConsumerSecret=settings.getString("uOneConsumerSecret", "");

		this.dBoxApiKey=settings.getString("dBoxApiKey", "");
		this.dBoxApiSecret=settings.getString("dBoxApiSecret", "");
		this.dBoxToken=settings.getString("dBoxToken", "");
		this.dBoxTokenSecret=settings.getString("dBoxTokenSecret", "");

		Log.d("CUSetting", "getSetting : "+this.boxApiKey);
		Log.d("CUSetting", "getSetting : "+this.boxToken);
		Log.d("CUSetting", "getSetting : "+this.uOneToken);
		Log.d("CUSetting", "getSetting : "+this.uOneTokenSecret);
		Log.d("CUSetting", "getSetting : "+this.uOneConsumer);
		Log.d("CUSetting", "getSetting : "+this.uOneConsumerSecret);
		Log.d("CUSetting", "getSetting : "+this.dBoxApiKey);
		Log.d("CUSetting", "getSetting : "+this.dBoxApiSecret);
		Log.d("CUSetting", "getSetting : "+this.dBoxToken);
		Log.d("CUSetting", "getSetting : "+this.dBoxTokenSecret);
	}
}
