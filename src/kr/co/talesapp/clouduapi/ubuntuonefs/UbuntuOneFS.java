package kr.co.talesapp.clouduapi.ubuntuonefs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import kr.co.talesapp.clouduapi.CUError;
import kr.co.talesapp.clouduapi.CUFile;
import kr.co.talesapp.clouduapi.CloudFS;
import kr.co.talesapp.clouduapi.OAuthTokens;
import kr.co.talesapp.clouduapi.interfaces.ICloudFS.CloudType;

public class UbuntuOneFS extends CloudFS {
	Properties prop;
	DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpContext localContext = new BasicHttpContext();
	String authHeader=null;
	
	public CUError createFolder(String name, String parent_path){
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("UbuntuOneFS.createFolder");
			if(parent_path.equals("/")) {
				fullPath+="/~/Ubuntu%20One/"+name;
			} else {
				fullPath+=parent_path+"/"+name;
				fullPath=fullPath.replaceAll("%2F", "/");
				fullPath=fullPath.replaceAll("%7E", "~");
				fullPath=fullPath.replaceAll("\\+", "%20");
				fullPath=fullPath.replaceAll(" ", "%20");
			}
			if(debug) {
				System.out.println("ubuntu createFolder : "+fullPath);
			}
			HttpPut httpPut=new HttpPut(fullPath);
			httpPut.setHeader("Authorization", authHeader);
			StringEntity input;
			input = new StringEntity("{ \"kind\": \"directory\" }");
			input.setContentType("application/json");
			httpPut.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPut, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug){
				System.out.println("ubuntuone createFolder : "+httpPut.getURI());
				System.out.println("ubuntuone createFolder : "+responseString);
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
			String fullPath=prop.getProperty("UbuntuOneFS.uploadFile");
			if(folder_path.equals("/")) {
				fullPath+="/~/Ubuntu%20One/"+fileName;
			} else {
				fullPath+=folder_path+"/"+fileName;
				fullPath=fullPath.replaceAll("%2F", "/");
				fullPath=fullPath.replaceAll("%7E", "~");
				fullPath=fullPath.replaceAll("\\+", "%20");
				fullPath=fullPath.replaceAll(" ", "%20");
			}
			HttpPut httpPut=new HttpPut(fullPath);
			if(debug){
				System.out.println("ubuntuone uploadFile : "+httpPut.getURI());
			}
			httpPut.setHeader("Authorization", authHeader);
			FileEntity entity=new FileEntity(file, ContentType.create(new MimetypesFileTypeMap().getContentType(file)));
			httpPut.setEntity(entity);
			HttpResponse response;
			response = httpclient.execute(httpPut, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug){
				System.out.println("ubuntuone uploadFile : "+responseString);
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
			String fullPath=prop.getProperty("UbuntuOneFS.downloadFile");
			fullPath+=path;
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			fullPath=fullPath.replaceAll(" ", "%20");
			if(debug){
				System.out.println("ubuntuone downloadFile : "+fullPath);
			}
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.setHeader("Authorization", authHeader);
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			byte[] responseString=EntityUtils.toByteArray(responseEntity);
			if(debug){
				//System.out.println("ubuntuone downloadFile : "+responseString+", "+response.getStatusLine().getStatusCode());
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
		try {
			String fullPath=prop.getProperty("UbuntuOneFS.move");
			fullPath+=URLEncoder.encode(path, "UTF-8");
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			newPath=URLEncoder.encode(newPath, "UTF-8");
			newPath=newPath.replaceAll("%2F", "/");
			newPath=newPath.replaceAll("%7E", "~");
			newPath=newPath.replaceAll("\\+", "%20");
			newPath=newPath.replaceAll("/~/Ubuntu%20One", "");
			
			HttpPut httpPut=new HttpPut(fullPath);
			httpPut.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			input = new StringEntity("{ \"path\": \""+newPath+"\" }");
			input.setContentType("application/json");
			httpPut.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPut, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("ubuntuone moveFile : "+httpPut.getURI());
				System.out.println("ubuntuone moveFile : "+responseString);
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
		return renameFile(path, newPath);
	}
	
	public CUError renameFile(String path, String newPath){
		CUError err=new CUError();
		try {
			String fullPath=prop.getProperty("UbuntuOneFS.move");
			fullPath+=URLEncoder.encode(path, "UTF-8");
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			newPath=URLEncoder.encode(newPath, "UTF-8");
			newPath=newPath.replaceAll("%2F", "/");
			newPath=newPath.replaceAll("%7E", "~");
			newPath=newPath.replaceAll("\\+", "%20");
			newPath=newPath.replaceAll("/~/Ubuntu%20One", "");
			
			HttpPut httpPut=new HttpPut(fullPath);
			httpPut.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			input = new StringEntity("{ \"path\": \""+newPath+"\" }");
			input.setContentType("application/json");
			httpPut.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPut, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("ubuntuone moveFile : "+httpPut.getURI());
				System.out.println("ubuntuone moveFile : "+responseString);
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
	
	public CUError copyFolder(String path, String newPath) {
		return copyFile(path, newPath);
	}
	
	public CUError copyFile(String path, String newPath) {
		CUError err=new CUError();
		//try {
			// ubuntu one does not support copy.
			/*
			String fullPath=prop.getProperty("UbuntuOneFS.copy");
			fullPath+=URLEncoder.encode(path, "UTF-8");
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			newPath=URLEncoder.encode(path, "UTF-8");
			newPath=newPath.replaceAll("%2F", "/");
			newPath=newPath.replaceAll("%7E", "~");
			newPath=newPath.replaceAll("\\+", "%20");
			newPath=newPath.replaceAll("/~/Ubuntu%20One", "");
			
			HttpPut httpPut=new HttpPut(fullPath);
			httpPut.setHeader("Authorization", authHeader);
			//JSONObject json=JSONObject.fromObject("{ \"path\": "+newPath+" }");
			StringEntity input;
			input = new StringEntity("{ \"path\": "+newPath+" }");
			input.setContentType("application/json");
			httpPut.setEntity(input);
			HttpResponse response;
			response = httpclient.execute(httpPut, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("ubuntuone copyFile : "+responseString);
			}
			*/
		err.error=CUError.NOT_IMPLEMENTED;
		err.errorString="not implemented";
		return err;
		//} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		//return 0;
	}

	public CUError deleteFolder(String path) {
		return deleteFile(path);
	}

	public CUError deleteFile(String path) {
		CUError err=new CUError();
		String fullPath=prop.getProperty("UbuntuOneFS.delete");
		try {
			fullPath+=URLEncoder.encode(path, "UTF-8");
			fullPath=fullPath.replaceAll("%2F", "/");
			fullPath=fullPath.replaceAll("%7E", "~");
			fullPath=fullPath.replaceAll("\\+", "%20");
			HttpDelete delete = new HttpDelete(fullPath);
			delete.setHeader("Authorization", authHeader);
			HttpResponse response;
			response = httpclient.execute(delete, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("ubuntuone deleteFile : "+responseString);
			}
			if(responseString.equals("\"\"")) {
				err.error=CUError.SUCCESS;
				err.errorString="success";
			} else {
				err.error=CUError.ERROR;
				err.errorString=responseString;
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
		int size=0;
		String kind=(String) map.get("kind");
		if(map.get("size")!=null) {
			size=(Integer) map.get("size");
		}
		String name=((String)map.get("path")).replaceAll(".*/", "");
		String path=(String)map.get("resource_path");
		String root=(String)map.get("parent_path");
		Date time;
		try {
			String tmp=((String) map.get("when_changed")).replaceAll("T", " ");
			tmp=tmp.replaceAll("Z", "");
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(tmp);
			if(kind.equals("file")) {
				cufile.setDir(false);
			} else {
				cufile.setDir(true);
			}
			cufile.setName(name);
			cufile.setPath(path);
			cufile.setRoot(root);
			cufile.setTime(time);
			cufile.setSize(size);
			cufile.setType(CloudType.UBUNTUONE);
			return cufile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CUFile> getFiles(String path) {
		try {
			String fullPath=prop.getProperty("UbuntuOneFS.getFolderTree");
			if(path!=null && !path.equals("") && !path.equals("/")) {
				fullPath+=URLEncoder.encode(path, "UTF-8");
				fullPath=fullPath.replaceAll("%2F", "/");
				fullPath=fullPath.replaceAll("%7E", "~");
				fullPath=fullPath.replaceAll("\\+", "%20");
				//fullPath+=path;
			} else if(path!=null && path.equals("/")){
				fullPath+="/~/Ubuntu%20One";
			}
			fullPath+="/?include_children=true";
			if(debug) {
				System.out.println("ubuntuone uri : "+fullPath);
			}
			HttpGet httpGet=new HttpGet(fullPath);
			httpGet.setHeader("Authorization", authHeader);
			if(debug) {
				System.out.println("ubuntuone auth header : "+authHeader);
			}
			HttpResponse response;
			response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println("ubuntuone getFiles : "+responseString);
			}
			JSONObject json=JSONObject.fromObject(responseString);
			JSONArray children=json.getJSONArray("children");
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
		}
		return null;
	}

	public UbuntuOneFS(Properties prop) {
		this.prop=prop;
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
	/*
	@Override
	public OAuthTokens getAuthorize(OAuthTokens token, String callbackUrl) {
		return token;
	}
	*/
	
	@Override
	public OAuthTokens getAccessToken(OAuthTokens token) {
		try {
			this.token=token.getOAuthToken();
			this.token_secret=token.getOAuthTokenSecret();
			this.consumer_key=token.getConsumerKey();
			this.consumer_secret=token.getConsumerSecret();
			genAuthHeader();
			
			// HttpGet httpGet=new HttpGet(prop.getProperty("UbuntuOneFS.requestToken")+"?"+URLEncoder.encode("{'ws.op': 'authenticate', 'token_name': 'Ubuntu One @ CloudUAPI'}", "UTF-8"));
			HttpGet httpGet=new HttpGet(prop.getProperty("UbuntuOneFS.accessToken"));
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader("Authorization", authHeader);
			if(debug) {
				System.out.println(httpGet.getURI().toString());
				Header[] headers=httpGet.getAllHeaders();
				for(Header header: headers) {
					System.out.println(header.toString());
				}
			}
			HttpResponse response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			if(debug) {
				System.out.println(responseString);
			}
			EntityUtils.consume(responseEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return token;
	} 
	
	@Override
	public OAuthTokens getRequestToken(String email, String passwd) {
		this.email=email;
		this.passwd=passwd;
		
		try {
			// HttpGet httpGet=new HttpGet(prop.getProperty("UbuntuOneFS.requestToken")+"?"+URLEncoder.encode("{'ws.op': 'authenticate', 'token_name': 'Ubuntu One @ CloudUAPI'}", "UTF-8"));
			HttpGet httpGet=new HttpGet(prop.getProperty("UbuntuOneFS.requestToken")+"?ws.op=authenticate&token_name="+URLEncoder.encode("Ubuntu One @ CloudUAPI", "UTF-8"));
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader("Authorization", "Basic "+new String(new Base64().encode((email+":"+passwd).getBytes("UTF-8")), "UTF-8"));
			//System.out.println(httpGet.getURI().toString());
			//Header[] headers=httpGet.getAllHeaders();
			//for(Header header: headers) {
				//System.out.println(header.toString());
			//}
			HttpResponse response = httpclient.execute(httpGet, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			//System.out.println(responseString);
			JSONObject jsonObj=JSONObject.fromObject(responseString);
			oauth_token.setOAuthToken(jsonObj.getString("token"));
			oauth_token.setOAuthTokenSecret(jsonObj.getString("token_secret"));
			oauth_token.setConsumerKey(jsonObj.getString("consumer_key"));
			oauth_token.setConsumerSecret(jsonObj.getString("consumer_secret"));
			EntityUtils.consume(responseEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		/*
        DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(prop.getProperty("UbuntuOneFS.requestToken"));
        Random RAND = new Random();
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();
		String header=String.format("OAuth oauth_signature_method=\"PLAINTEXT\", oauth_timestamp=\"%s\", oauth_nonce=\"%s\", oauth_consumer_key=\"ubuntuone\", oauth_secret=\"hammertime\", oauth_signature=\"hammertime%%26\", oauth_callback=\"null\"", String.valueOf(timestamp), String.valueOf(nonce));
		httpPost.setHeader("Authorization", header);
		try {
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = httpclient.execute(httpPost, localContext);
			HttpEntity responseEntity = response.getEntity();
			String responseString=EntityUtils.toString(responseEntity);
			System.out.println(responseString);
			Map<String, String> map = getQueryMap(responseString);
			oauth_token.setOAuthToken(map.get("oauth_token"));
			oauth_token.setOAuthTokenSecret(map.get("oauth_token_secret"));
			EntityUtils.consume(responseEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		*/
		return oauth_token;
	}
}
