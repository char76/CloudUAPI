package kr.co.talesapp.clouduapi.dropboxfs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import kr.co.talesapp.clouduapi.CUError;
import kr.co.talesapp.clouduapi.CUFile;
import kr.co.talesapp.clouduapi.CloudFS;
import kr.co.talesapp.clouduapi.OAuthTokens;

public class DropboxFS extends CloudFS {
	Properties prop;
	HttpContext localContext = new BasicHttpContext();
    DefaultHttpClient httpclient = new DefaultHttpClient();
	String authHeader=null;
	
	public CUError createFolder(String name, String parent_path){
		CUError err=new CUError();
		try {
			HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.createFolder"));
			List<NameValuePair> param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("root", prop.getProperty("DropboxFS.root")));
			param.add(new BasicNameValuePair("path", parent_path+"/"+name));
			httpPost.addHeader("Authorization", authHeader);
			HttpResponse response;
			httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {				
				System.out.println("dropbox createFolder : "+responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			if(json.get("error")!=null) {
				err.error=CUError.ERROR;
				err.errorString=json.getString("error");
			} else {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}
	public CUError uploadFile(File file, String fileName, String folder_path) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("DropboxFS.uploadFile")+"/"+prop.getProperty("DropboxFS.root")+"/"+folder_path+"/"+fileName;
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			fullPath=fullPath.replaceAll(" ", "%20");
			HttpPost httpPost=new HttpPost(fullPath);
			httpPost.addHeader("Authorization", authHeader);
			HttpResponse response;
			FileEntity entity=new FileEntity(file, ContentType.create(new MimetypesFileTypeMap().getContentType(file)));
			httpPost.setEntity(entity);
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {				
				System.out.println(responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			if(json.get("error")!=null) {
				err.error=CUError.ERROR;
				err.errorString=json.getString("error");
			} else {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}
	public CUError downloadFile(String path) {
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("DropboxFS.downloadFile")+"/"+prop.getProperty("DropboxFS.root")+"/"+path;
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			fullPath=fullPath.replaceAll(" ", "%20");
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.addHeader("Authorization", authHeader);
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			byte[] responseString=EntityUtils.toByteArray(responseEntity);
			if(debug) {
				//System.out.println(responseString);
			}
			if(response.getStatusLine().getStatusCode()==200) {
				err.error=CUError.SUCCESS;
				//err.errorString="success";
				err.body=responseString;
				err.contentLength=responseEntity.getContentLength();
				err.contentType=responseEntity.getContentType();
			} else {
				JSONObject json=JSONObject.fromObject(responseString);
				if(json!=null && json.getString("type")!=null && json.getString("type").equals("error")) {
					err.error=CUError.ERROR;
					err.errorString=json.getString("code");
				} else {
					err.error=CUError.SUCCESS;
					err.errorString="success";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}
	public CUError moveFolder(String path, String newPath){
		return moveFile(path, newPath);
	}
	
	public CUError moveFile(String path, String newPath){
		CUError err=new CUError();
		HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.move"));
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("root", prop.getProperty("DropboxFS.root")));
		param.add(new BasicNameValuePair("from_path", path));
		param.add(new BasicNameValuePair("to_path", newPath));
		httpPost.addHeader("Authorization", authHeader);
		HttpResponse response;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {				
				System.out.println(path+", "+responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			if(json.get("error")!=null) {
				err.error=CUError.ERROR;
				err.errorString=json.getString("error");
			} else {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}
	
	public CUError renameFolder(String path, String newPath){
		return moveFile(path, newPath);
	}
	
	public CUError renameFile(String path, String newPath){
		return moveFile(path, newPath);
	}
	
	public CUError copyFolder(String path, String newPath) {
		return copyFile(path, newPath);
	}
	
	public CUError copyFile(String path, String newPath) {
		CUError err=new CUError();
		HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.copy"));
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("root", prop.getProperty("DropboxFS.root")));
		param.add(new BasicNameValuePair("from_path", path));
		param.add(new BasicNameValuePair("to_path", newPath));
		httpPost.addHeader("Authorization", authHeader);
		HttpResponse response;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {				
				System.out.println(responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			if(json.get("error")!=null) {
				err.error=CUError.ERROR;
				err.errorString=json.getString("error");
			} else {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}

	public CUError deleteFolder(String path) {
		return deleteFile(path);
	}
	
	public CUError deleteFile(String path) {
		CUError err=new CUError();
		HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.delete"));
		/*
		HttpParams param=new BasicHttpParams();
		param.setParameter("root", prop.getProperty("DropboxFS.root"));
		param.setParameter("path", path);
		httpPost.setParams(param);
		*/
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("root", prop.getProperty("DropboxFS.root")));
		param.add(new BasicNameValuePair("path", path));
		httpPost.addHeader("Authorization", authHeader);
		HttpResponse response;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
			response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println(responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			if(json.getBoolean("is_deleted")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else if(json.get("error")!=null) {
				err.error=CUError.UNKNOWN;
				err.errorString=json.getString("error");
			} else {
				err.error=CUError.UNKNOWN;
				err.errorString="unknown error";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err.error=CUError.EXCEPTION;
			err.errorString="exception";
		}
		return err;
	}

	public CUFile genCUFile(Map map) {
		CUFile cufile=new CUFile();
		boolean kind=(Boolean)map.get("is_dir");
		int size=(Integer) map.get("bytes");
		String name=((String)map.get("path")).replaceAll(".*/", "");
		String path=(String)map.get("path");
		String root=(String)map.get("root");
		Date time;
		try {
			time = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(((String) map.get("modified")).replaceAll("\\p{Cntrl}", ""));
			//time = new SimpleDateFormat("yyyy-dd-MM+HH:mm:ss").parse((String) map.get("modified"));
			cufile.setDir(kind);
			cufile.setName(name);
			cufile.setPath(path);
			cufile.setRoot(root);
			cufile.setTime(time);
			cufile.setSize(size);
			cufile.setType(CloudType.DROPBOX);
			return cufile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
    public void genAuthHeader() {
        Random RAND = new Random();
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();

        if(authHeader==null) {
        	authHeader="OAuth realm=\"\", oauth_version=\"1.0\", " +
				"oauth_nonce=\""+String.valueOf(nonce)+"\", " +
				"oauth_timestamp=\""+String.valueOf(timestamp)+"\", " +
				"oauth_consumer_key=\""+consumer_key+"\", " +
				"oauth_token=\""+token+"\", " +
				"oauth_signature_method=\"PLAINTEXT\", " +
				"oauth_signature=\""+consumer_secret+"%26"+token_secret+"\"";
        }
    }

	public List<CUFile> getFiles(String path) {
		try {
			String fullPath=prop.getProperty("DropboxFS.getFolderTree")+"/"+prop.getProperty("DropboxFS.root");
			if(path!=null && !path.equals("") && !path.equals("/")) {
				fullPath+="/"+path;
			}
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			fullPath=fullPath.replaceAll(" ", "%20");
			fullPath+="/?list=true";
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.setHeader("Authorization", authHeader);
			if(debug) {
				System.out.println("dropbox auth header : "+authHeader);
			}
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("dropbox getFiles : "+responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			JSONArray children=json.getJSONArray("contents");
			List<CUFile> cufile=new ArrayList<CUFile>();
			for(int i=0;children!=null && i<children.size();i++){// obj : children){
				Map map=(Map)(children.get(i));
				if(debug) {
					System.out.println(map.toString());
				}
				cufile.add(genCUFile(map));
			}
			return cufile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public DropboxFS(Properties prop, String appKey, String appSecret) {
		this.token=appKey;
		this.token_secret=appSecret;
		this.prop=prop;
	}

	public DropboxFS(Properties prop) {
		this.prop=prop;
	}
	
	/*
	@Override
	public OAuthTokens getAuthorize(OAuthTokens token, String callbackUrl) {
		try {
			HttpGet httpGet=new HttpGet(prop.getProperty("DropboxFS.authorize")+"?oauth_token="+token.getOAuthToken()+"&oauth_consumer_key="+key+"&oauth_callback="+URLEncoder.encode(callbackUrl, "UTF-8"));
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			System.out.println(EntityUtils.toString(responseEntity));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return token;
	}
	*/
	
	@Override
	public OAuthTokens getAccessToken(OAuthTokens token) {
		HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.accessToken"));
		List<NameValuePair> auth=new ArrayList<NameValuePair>();
		auth.add(new BasicNameValuePair("oauth_token", token.getOAuthToken()));
		auth.add(new BasicNameValuePair("oauth_token_secret", token.getOAuthTokenSecret()));
		try {
	        Random RAND = new Random();
	        long timestamp = System.currentTimeMillis() / 1000;
	        long nonce = timestamp + RAND.nextInt();
			auth.add(new BasicNameValuePair("oauth_nonce", String.valueOf(nonce)));
			auth.add(new BasicNameValuePair("oauth_timestamp", String.valueOf(timestamp)));
			String header=String.format("OAuth oauth_signature_method=\"PLAINTEXT\", oauth_token=\"%s\", oauth_consumer_key=\"%s\", oauth_signature=\"%s&%s\"", token.getOAuthToken(), token.getConsumerKey(), token.getConsumerSecret(), token.getOAuthTokenSecret());
			httpPost.addHeader("Authorization", header);
			if(debug) {
				System.out.println(header);
			}
			//httpPost.setEntity(new UrlEncodedFormEntity(auth));
			HttpResponse response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println(header);
				System.out.println(responseString);
			}
			Map<String, String> map = getQueryMap(responseString);
			token.setOAuthToken(map.get("oauth_token"));
			token.setOAuthTokenSecret(map.get("oauth_token_secret"));
			//EntityUtils.consume(responseEntity);
			/*
			HttpEntity responseEntity = response.getEntity();
			responseEntity.getContent()
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return token;
	}

	@Override
	public OAuthTokens getRequestToken(String appKey, String appSecret) {
		this.token=appKey;
		this.token_secret=appSecret;
	
		HttpPost httpPost=new HttpPost(prop.getProperty("DropboxFS.requestToken"));
		/*
		HttpParams params=new BasicHttpParams();
		params.setParameter("oauth_consumer_key", key);
		params.setParameter("oauth_token", secret);
		params.setParameter("oauth_signature_method", "HMAC-SHA1");
		*/
		List<NameValuePair> auth=new ArrayList<NameValuePair>();
		auth.add(new BasicNameValuePair("oauth_consumer_key", token));
		auth.add(new BasicNameValuePair("oauth_token", token_secret));
		auth.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        Random RAND = new Random();
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();
		auth.add(new BasicNameValuePair("oauth_nonce", String.valueOf(nonce)));
		auth.add(new BasicNameValuePair("oauth_timestamp", String.valueOf(timestamp)));
		String header=String.format("OAuth oauth_signature_method=\"PLAINTEXT\", oauth_consumer_key=\"%s\", oauth_signature=\"%s&\"", token, token_secret);
		httpPost.addHeader("Authorization", header);
		try {
			HttpResponse response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("getRequestToken : "+responseString);
			}
			Map<String, String> map = getQueryMap(responseString);
			oauth_token.setOAuthToken(map.get("oauth_token"));
			oauth_token.setOAuthTokenSecret(map.get("oauth_token_secret"));
			//System.out.println("request token : "+responseString);
			//EntityUtils.consume(responseEntity);
			/*
			HttpEntity responseEntity = response.getEntity();
			responseEntity.getContent()
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return oauth_token;
	}
}
