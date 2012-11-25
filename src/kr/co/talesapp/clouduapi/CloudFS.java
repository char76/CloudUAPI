package kr.co.talesapp.clouduapi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import kr.co.talesapp.clouduapi.OAuthTokens;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS;

public class CloudFS implements ICloudFS {
	protected String email="";
	protected String passwd="";
	protected OAuthTokens oauth_token=new OAuthTokens();
	protected String consumer_key="";
	protected String consumer_secret="";
	protected String token="";
	protected String token_secret="";
	protected String key="";
	protected boolean debug=false;
	
	protected void init(String key, String token, String secret, String consumer, String consumer_secret) {
		this.key=key;
		this.token=token;
		this.token_secret=secret;
		this.consumer_key=consumer;
		this.consumer_secret=consumer_secret;
	}

	protected static Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	}  
	
	protected String getSignature(String url, String params) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		/**
		 * base has three parts, they are connected by "&": 1) protocol 2) URL
		 * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
		 */
		StringBuilder base = new StringBuilder();
		base.append("POST&");
		base.append(url);
		base.append("&");
		base.append(params);
		System.out.println(token+", "+base);
		//System.out.println("Stirng for oauth_signature generation:" + base);
		// yea, don't ask me why, it is needed to append a "&" to the end of
		// secret key.
		byte[] keyBytes = (token_secret + "&").getBytes("UTF-8");
		
		SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
		
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(key);
		
		// encode it, base64 it, change it to string and return.
		return new String(new Base64().encode(mac.doFinal(base.toString().getBytes(
		        "UTF-8"))), "UTF-8");
	}
	
	@Override
	public int createFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createFolder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteFolder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int downloadFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAccountInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFolderTree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OAuthTokens login() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OAuthTokens getRequestToken() {
		return null;
	}

	@Override
	public OAuthTokens getRequestToken(CloudType type) {
		return getRequestToken(type, null, null);
	}

	@Override
	public OAuthTokens getRequestToken(String appKey, String appSecret) {
		return null;
	}

	@Override
	public int moveFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveFolder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int uploadFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int copyFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int copypFolder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public OAuthTokens getRequestToken(CloudType type, String appKey, String appSecret) {
		return null;
	}

	public OAuthTokens getAccessToken(OAuthTokens token) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public OAuthTokens getAuthorize(OAuthTokens tokens, String callbackUrl) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}
